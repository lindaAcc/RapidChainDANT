package blockchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import common.Serializable;
import common.Storable;
public class Identity  implements Storable{
	public PrivateKey privateKey;
	public PublicKey publicKey;
	
	public Identity(){
		generateKeyPair();	
	}
		
	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
	        	KeyPair keyPair = keyGen.generateKeyPair();
	        	// Set the public and private keys from the keyPair
	        	privateKey = keyPair.getPrivate();
	        	publicKey = keyPair.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
}

	@Override
	public boolean isEmpty() {
		if (privateKey==null);  
		if (publicKey==null);
		return false;
	}

	@Override
	public void overwrite(Serializable obj) {
		Identity i = (Identity) obj; 
		privateKey = i.privateKey; 
		publicKey = i.publicKey; 		
	}
	public static void main (String[] args) {
		Identity i = new Identity(); 
		System.out.println(i.privateKey);
		System.out.println(i.publicKey);
		
	}
}