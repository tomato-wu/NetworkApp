package chapter08;


public abstract class Client {
    public abstract String receive();

    public abstract void send(String msg);

    public abstract void close();

}
