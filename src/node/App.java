package node;
import java.io.IOException;

import blockchain.Identity;
import blockchain.Pocket;
import common.Debuggable;
import common.Log;
import common.PrettyJsonSerialStrategy;
import common.Store;
import tcp.Address;

public class App extends Debuggable implements Runnable {
	public static volatile Store store = new Store("data");
	protected boolean runtests = false;
	protected Address addr;
	protected static final int defaultPort = 3023;
	protected static Identity id; 
	protected static volatile PeerTable peers;
	protected static volatile Pocket pocket;
	protected static volatile NodeServer server;
	
	public App() {
		this (defaultPort);
	}

	public App(int port) {
		this.addr = new Address("localhost", port);
		this.prefix = "APP";
		
		//set log level to info
		Log.start(store, 1);

	}
	
	public App(int port, boolean runtests) {
		this(port);
		Log.setLevel(2);
		this.runtests = runtests;
	}

	@Override
	public void run() {
		new Thread(new NodeServer(addr, 20)).start();
		try {
			// tests routetable
			peers = new PeerTable();
			peers.initFromDns();
			store.register(peers, "peers", new PrettyJsonSerialStrategy());
			store.load("peers");

			id = new Identity(); 
			store.register(id, "identity", new PrettyJsonSerialStrategy());
			store.load("identity");
			if(id.isEmpty()) {
				id.generateKeyPair();
				store.save("identity");
			}
			
			pocket = new Pocket(4);
			store.register(pocket, "pocket", new PrettyJsonSerialStrategy());
			store.load("pocket");
			
			if (runtests) {
				runTests();
			}

		} catch (IOException e) {
			Log.error(e);
		}
	}
	
	protected void runTests() {
		

		// tests
		try {
			
			//tests routetable
//			routeTable.add(new Node("128.78.51.131", 3032));
			peers.add(new Node("localhost", 3023));
			store.save("peers");
			store.load("peers");
			
			//tests pocket
			pocket.tests();
			store.save("pocket");
//			store.load("pocket");
			
			Log.debug(peers);
			Log.debug(pocket);
			Log.debug(pocket.isChainValid());
			PeerTable test = (PeerTable) peers.requestAll(new PeerTable());
			Log.debug(test);
			Pocket test2 = (Pocket) peers.requestAll(new Pocket());
			Log.debug(test2);
			peers.putAll(test);
			store.save("peers");
			Log.debug(peers);
			
		} catch (IOException e) {
			Log.error(e);
		}
	}

}
