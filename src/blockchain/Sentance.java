package blockchain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Sentance implements Serializable{

	public String transactionId; // this is also the hash of the transaction.
	public String value;

	// Constructor:
	public Sentance(String value) {
		this.value = value;
	}
	// This Calculates the transaction hash (which will be used as its Id)
	public String calculateHash() {
		return StringUtil.applySha256(value);
	}

	public boolean isEmpty() {
		if (value.isEmpty())
			return true;
		else
			return false;
	}

	public String toString() {
		return "data : " + this.value.toString();
	}

}