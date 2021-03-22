package eip.DocumentMessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Exporter {
    // the name of the queue to be used to communicate
    private final static String QUEUE_NAME = "products";
    // the connection to the broker
    Connection connection = null;
    // the channel to communicate with the broker
    Channel channel = null;
    public Exporter() throws Exception {
        // Create a factory of connections to the broker
        ConnectionFactory factory = new ConnectionFactory();
        // Specify the address of the broker server
        factory.setHost("localhost");
        // Create a new connection to the broker
        connection = factory.newConnection();
        // Create a new channel within this connection
        // to consume and to produce messages
        channel = connection.createChannel();
        // Declare the queue to be used with the parameters:
        // durable:false, exclusive: false, autodelete:false,
        // additional-arguments:null
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    }
    public void exportObject(Product p)  {
// send a String representation of the product to the Queue
        try {
            channel.basicPublish("", QUEUE_NAME, null, p.productToString().getBytes("UTF-8"));
        } catch (Exception ex) {
            System.out.println("Error encoding "+ex);
        }
    }
    public void close()
    {
        try {
            channel.close();
            connection.close();
        } catch (Exception ex) {
            Logger.getLogger(Exporter.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
