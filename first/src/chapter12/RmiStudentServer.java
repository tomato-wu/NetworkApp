package chapter12;

import chapter12.rmi.RmiKitService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RmiStudentServer {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            RmiKitService rmiKitService = new RmiKitServiceImpl();
            registry.rebind("RmiKitService", rmiKitService);

            System.out.println("发布了一个RmiKitService RMI服务");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
