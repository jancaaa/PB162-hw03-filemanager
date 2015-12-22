package cz.muni.fi.pb162.hw03.impl;

/**
 * @author: Jana Zahradnickova,  UCO 433598
 * @version: 16. 12. 2015
 */
public class Main {
    /**
     * My main class.
     *
     * @param args input parameters
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage info: Wrong parameters");
            System.exit(1);
        }
        FileManagerImpl fm = new FileManagerImpl();
        try {
            fm.executeJob(args[0], args[1]);
        } catch (Exception e) {
            System.out.println("Something get wrong during execution - log: " + args[1]);
            System.exit(1);
        }

        System.out.println("Done - log: " + args[1]);

    }
}
