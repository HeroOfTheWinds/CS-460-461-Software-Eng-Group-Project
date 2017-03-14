using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;

#if NETFX_CORE || WINDOWS_PHONE
using System.Threading.Tasks;
using Windows.Networking;
using Windows.Networking.Sockets;
using Windows.Storage.Streams;
#else

#endif



    public class TcpClient
    {

#if NETFX_CORE || WINDOWS_PHONE
        private StreamSocket client;
#else
        private Socket client;
#endif

#if NETFX_CORE || WINDOWS_PHONE
        //client = null;
        DataWriter _writer;

        private async Task EnsureSocket(string hostName, int port)
        {
            try
            {
                var host = new HostName(hostName);
                client = new StreamSocket();
                await client.ConnectAsync(host, port.ToString(), SocketProtectionLevel.SslAllowNullEncryption);
            }
            catch (Exception ex)
            {
                // If this is an unknown status it means that the error is fatal and retry will likely fail.
                if (Windows.Networking.Sockets.SocketError.GetStatus(ex.HResult) == Windows.Networking.Sockets.SocketErrorStatus.Unknown)
                {
                    // TODO abort any retry attempts on Unity side
                    throw;
                }
            }
        }

        private async Task WriteToOutputStreamAsync(byte[] bytes)
        {

            if (client == null) return;
            _writer = new DataWriter(client.OutputStream);
            _writer.WriteBytes(bytes);

            var debugString = UTF8Encoding.UTF8.GetString(bytes, 0, bytes.Length);

            try
            {
                await _writer.StoreAsync();
                await client.OutputStream.FlushAsync();

                _writer.DetachStream();
                _writer.Dispose();
            }
            catch (Exception exception)
            {
                // If this is an unknown status it means that the error if fatal and retry will likely fail.
                if (Windows.Networking.Sockets.SocketError.GetStatus(exception.HResult) == Windows.Networking.Sockets.SocketErrorStatus.Unknown)
                {
                    // TODO abort any retry attempts on Unity side
                    throw;
                }
            }
        }
#endif

    public TcpClient()
    {
#if NETFX_CORE
        client = new StreamSocket();
#else
        client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
#endif
    }

    public int SendTimeout { get; set; }
    public int ReceiveTimeout { get; set; }

    public void Connect(_IPEndPoint EP)
    {
        string hostName = EP.AddressFamily.ToString();
        int port = EP.Port;
#if NETFX_CORE
        var thread = EnsureSocket(hostName, port);
        thread.Wait();
#else
        IPEndPoint remoteEP = new IPEndPoint(IPAddress.Parse(EP.AddressFamily), port);
        client.Connect(remoteEP);
#endif
    }


#if NETFX_CORE
    //no socket flags in windows networking, using int
    public void Send(byte[] buffer, int size, int flags)
    {
        //not implemented
    }
#else
    public void Send(byte[] buffer, int size, SocketFlags flags)
    {
        client.Send(buffer, size, flags);
    }
#endif


#if NETFX_CORE
    //no socket flags in windows networking, using int
    public void Receive(byte[] buffer, int size, int flags)
    {
            //not implemented
    }
#else
    public void Receive(byte[] buffer, int size, SocketFlags flags)
    {
        client.Receive(buffer, size, flags);
    }
#endif



#if NETFX_CORE
    //no socket flags in windows networking, using int
    public void Receive(byte[] buffer, int offset, int size, int flags)
    {
            //not implemented
    }
#else
    public void Receive(byte[] buffer, int offset, int size, SocketFlags flags)
    {
        client.Receive(buffer, offset, size, flags);
    }
#endif


#if NETFX_CORE
    //no socket flags in windows networking, using int
    public void BeginReceive(byte[] buffer, int offset, int size, int socketFlags, AsyncCallback callback, object state)
    {
            //not implemented
    }
#else
    public void BeginReceive(byte[] buffer, int offset, int size, SocketFlags socketFlags, AsyncCallback callback, object state)
    {
        client.BeginReceive(buffer, offset, size, socketFlags, callback, state);
    }
#endif


    public void EndReceive(IAsyncResult ar)
    {
#if NETFX_CORE
            //not implemented
#else
        client.EndReceive(ar);
#endif
    }

#if NETFX_CORE
    //no socket shutdown enum with windows networking, I thing .dispose() handles shutdown
#else
    public void Shutdown(SocketShutdown how)
    {
        client.Shutdown(how);
    }
#endif
 

    public Stream GetStream()
        {
#if NETFX_CORE || WINDOWS_PHONE
            if (client == null) return null;
            return client.InputStream.AsStreamForRead();
#else
            throw new NotImplementedException();
#endif
        }

        public Stream GetOutputStream()
        {
#if NETFX_CORE || WINDOWS_PHONE
            if (client == null) return null;
            return client.OutputStream.AsStreamForWrite();
#else
            throw new NotImplementedException();
#endif
        }

        public void Close()
        {
#if NETFX_CORE || WINDOWS_PHONE
            if (client != null)
            {
                client.Dispose();
            }
#else
        client.Close();
#endif
        }

        public void Send(byte[] bytes)
        {
#if NETFX_CORE || WINDOWS_PHONE
            var thread = WriteToOutputStreamAsync(bytes);
            thread.Wait();
#else
            throw new NotImplementedException();
#endif
        }


    }

public struct _IPEndPoint
{
    public _IPEndPoint(string a, int p)
    {
        AddressFamily = a;
        Port = p;
    }

    public string AddressFamily;
    public int Port;
}