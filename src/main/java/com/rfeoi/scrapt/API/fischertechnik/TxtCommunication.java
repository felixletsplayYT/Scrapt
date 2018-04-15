package com.rfeoi.scrapt.API.fischertechnik;


import java.net.Socket;
import java.util.HashMap;

/// <summary>
/// This class manages the TCP/IP communication between the TXT and us.
/// </summary>
public class TxtCommunication {
    public boolean Connected = false;



    private HashMap<String, String> controllerNameCache;

    private boolean requestedStop;
    private Socket _socket;

    public TxtInterface TxtInterface;
    private Thread networkingTaskQueue;

    public TxtCommunication(TxtInterface txtInterface) {
        TxtInterface = txtInterface;

        networkingTaskQueue = new Thread("TXT Communication");
        controllerNameCache = new HashMap<>();
    }

    public void OpenConnection() throws Exception {
        if (Connected) {
            throw new Exception("Already connected!");
        }

        requestedStop = false;

        Exception exception = null;

        networkingTaskQueue = new Thread(() -> {
            try {
                _socket = new Socket();
                Connected = _socket.isConnected();
            } catch (Exception e) {
                e.printStackTrace();
                Connected = false;
            }
        });
        networkingTaskQueue.start();





        if (exception != null) {
            Connected = false;
            throw exception;
        }
    }

   /** public void CloseConnection() {
        if (!Connected) {
            throw new InvalidOperationException("Not connected!");
        }

        requestedStop = true;

        Exception exception = null;

        networkingTaskQueue.DoWorkInQueue(() = >
                {
        try {
            _socket.Shutdown(SocketShutdown.Both);
            _socket.Close();
            Connected = false;
        } catch (SocketException e) {
            Console.WriteLine(e.Message);
            exception = e;
            Connected = false;
        }
        },true);

        if (exception != null) {
            Connected = false;
            throw exception;
        }
    }

    public void SendCommand(CommandBase command, ResponseBase response) {
        Exception exception = null;

        networkingTaskQueue.DoWorkInQueue(() = >
                {
        try {
            // Send the command
            _socket.Send(command.GetByteArray());


            var responseBytes = new byte[response.GetResponseLength()];

            // Receive the response
            Receive(_socket, responseBytes, responseBytes.Length);


            uint responseId = BitConverter.ToUInt32(responseBytes, 0);

            if (responseId != response.ResponseId) {
                // Set an exception when the received response id is not the same as the expected response id
                // The exception is thrown at the end
                exception =
                        new InvalidOperationException(
                                $"The response id ({responseId}) is not the same response id ({response.ResponseId}) as expected");
            } else {
                // Set the values of the given response object
                response.FromByteArray(responseBytes);
            }
        } catch (Exception ex) {
            Console.WriteLine(ex.Message);
            exception = ex;
        }

        },true);


        if (exception != null) {
            if (!(exception is InvalidOperationException))
            {
                Connected = false;
            }

            throw exception;
        }
    }

    private int Receive(Socket socket, byte[] buffer, int count) {
        // Wait until enough bytes are received
        while (socket.Available < count && !requestedStop) {
            Thread.Sleep(5);
        }

        return socket.Receive(buffer, 0, count, SocketFlags.None);
    }


    public string RequestControllerName(string address) {
        // If we cached the address already: return the controller name
        if (controllerNameCache.ContainsKey(address)) {
            return controllerNameCache[address];
        }


        try {
            // Connect to the interface
            var ipEndPoint = new IPEndPoint(IPAddress.Parse(address), TxtInterface.ControllerIpPort);
            var socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp) {
                SendTimeout =1000,
                ReceiveTimeout =1000
            };
            socket.Connect(ipEndPoint);


            // Send the command
            socket.Send(new CommandQueryStatus().GetByteArray());

            var response = new ResponseQueryStatus();

            var responseBytes = new byte[response.GetResponseLength()];


            // Receive the response
            Receive(socket, responseBytes, responseBytes.Length);

            // Close the socket
            socket.Shutdown(SocketShutdown.Both);
            socket.Close();
            socket.Dispose();

            // Process the response
            response.FromByteArray(responseBytes);

            controllerNameCache.Add(address, response.GetControllerName());

            return response.GetControllerName();
        } catch (Exception) {
            return string.Empty;
        }
    }

    public bool IsValidInterface(string address) {
        return !string.IsNullOrEmpty(RequestControllerName(address));
    }

    public void Dispose() {
        // Close the connection before disposing the task queue
        if (Connected) {
            CloseConnection();
        }

        ((IDisposable) networkingTaskQueue).Dispose();
        ((IDisposable) _socket).Dispose();
    }
} */
        }