package com.ben.hashgraph;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.HederaNetworkException;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.contract.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.contract.ContractId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.file.FileCreateTransaction;
import com.hedera.hashgraph.sdk.file.FileId;

public class UploadAndCreateSmartContract {

	public static void main(String[] args) throws IOException, HederaNetworkException, HederaException {
		
		String accountId = System.getenv("HASHGRAPH_ACCOUNTID"); // Expecting your account id: 0.0.xxxx
		String privateKey = System.getenv("HASHGRAPH_PRIVATEKEY"); // Expecting your private key
		
		String nodeId = "0.0.3";
		String nodeAddress = "0.testnet.hedera.com:50211";
		
		// The first step is to connect to Hedera
		Client client = new Client(Map.of(AccountId.fromString(nodeId), nodeAddress));
		client.setOperator(AccountId.fromString(accountId), Ed25519PrivateKey.fromString(privateKey));
		
		// Then, we need to upload the *.bin file as file in Hedera
		// Keep in mind that there is a limit of 4K, so if your file is bigger, you'll need to upload the first 4K bytes,
		// and then use the FileAppend function to add the missing bytes.
		byte[] content = IOUtils.toByteArray(UploadAndCreateSmartContract.class.getResourceAsStream("/HelloWorld.bin"));
		FileCreateTransaction fileCreateTx = new FileCreateTransaction(client)
			// Use the same key as the operator to "own" this file
			.addKey(Ed25519PrivateKey.fromString(privateKey).getPublicKey())
			.setExpirationTime(Instant.now().plus(90, ChronoUnit.DAYS))
			.setTransactionFee(500000000L)
			.setContents(content);
		
		// We are ready to create the file !
		System.out.println("We are ready to create your file!");
		TransactionReceipt receipt = fileCreateTx.executeForReceipt();
		FileId fileId = receipt.getFileId();
		System.out.println("The File ID is: " + fileId);
		
		// Now, let's create the smart contract
		
		ContractCreateTransaction contractCreateTx = new ContractCreateTransaction(client)
			.setGas(100000000L)
			.setTransactionFee(1500000000L)
			.setAutoRenewPeriod(Duration.ofDays(90))
			.setBytecodeFile(fileId);
		
		// We are ready to create the smart contract !
		System.out.println("We are ready to create the smart contract!");
		receipt = contractCreateTx.executeForReceipt();
		ContractId contractId = receipt.getContractId();
		System.out.println("The Smart Contract ID is: " + contractId);
		
	}

}
