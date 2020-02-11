package qconlondon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Random;
import java.util.stream.IntStream;

import uk.ac.manchester.tornado.api.TaskSchedule;
import uk.ac.manchester.tornado.api.annotations.Parallel;
import uk.ac.manchester.tornado.api.TornadoDriver;
import uk.ac.manchester.tornado.api.runtime.TornadoRuntime;

public class Server extends Thread {

    public static final int PORT_NUMBER = 8081;

    protected Socket socket;

    private TaskSchedule ts;

    private float[] a;
    private float[] b;

    private Server(Socket socket) {
        this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());

        a = new float[100];
        b = new float[100];

        Random r = new Random();
        IntStream.range(0, 100).parallel().forEach(idx -> {
            a[idx] = r.nextFloat();
        });

        // @formatter:off
        ts = new TaskSchedule("s0")
                .streamIn(a)
                .task("t0", Server::vectorAddition, a, b)
                .streamOut(b);
        // @formatter:on

        start();
    }

    private static void vectorAddition(final float[] a, final float[] b) {
        for (@Parallel int i = 0; i < a.length; i++) {
            b[i] = a[i] + a[i];
        }
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;

        TornadoDriver driver = TornadoRuntime.getTornadoRuntime().getDriver(0);
        final int maxDevices = driver.getDeviceCount();

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String request;

            int deviceNumber = 0;
            while ((request = br.readLine()) != null) {
                try {
                    deviceNumber = Integer.parseInt(request);
                } catch (NumberFormatException e) {
                    deviceNumber = 0;
                }

                if (deviceNumber >= maxDevices) {
                    System.out.println("[Warning] max " + maxDevices + " devices");
                    deviceNumber = maxDevices - 1;
                }

                ts.mapAllTo(driver.getDevice(deviceNumber));

                System.out.println("Selecting the device: " + request);
                request += '\n';
                out.write(request.getBytes());

                ts.execute();
            }

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Server Example");
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT_NUMBER);
            while (true) {
                new Server(server.accept());
            }
        } catch (IOException ex) {
            System.out.println("Unable to start server.");
        } finally {
            try {
                if (server != null)
                    server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}