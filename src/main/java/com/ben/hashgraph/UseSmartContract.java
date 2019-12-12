package com.ben.hashgraph;

import java.util.Map;

import com.hedera.hashgraph.sdk.CallParams;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.FunctionResult;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.HederaNetworkException;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.contract.ContractCallQuery;
import com.hedera.hashgraph.sdk.contract.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.contract.ContractId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;

public class UseSmartContract {
	
	public static void main(String[] args) throws HederaNetworkException, HederaException {
		
		String accountId = System.getenv("HASHGRAPH_ACCOUNTID"); // Expecting your account id: 0.0.xxxx
		String privateKey = System.getenv("HASHGRAPH_PRIVATEKEY"); // Expecting your private key
		
		String nodeId = "0.0.3";
		String nodeAddress = "0.testnet.hedera.com:50211";
		
		String smartContractId = "0.0.139500"; // Replace with your smart contract ID
		String yourName = "Ben C"; // Change with your name
		
		// The first step is to connect to Hedera
		Client client = new Client(Map.of(AccountId.fromString(nodeId), nodeAddress));
		client.setOperator(AccountId.fromString(accountId), Ed25519PrivateKey.fromString(privateKey));
		
		// And now, let's say hello
		//sayHello(client, smartContractId);

		// You can introduce yourself
		introduceYourself(client, smartContractId, yourName);
        
		// And say hello again
		sayHello(client, smartContractId);
		
	}
	
	private static void sayHello(Client client, String smartContractId) throws HederaNetworkException, HederaException {
		// Let's test
		ContractCallQuery query = new ContractCallQuery(client)
			.setContractId(ContractId.fromString(smartContractId))
			.setGas(30000)
			.setFunctionParameters(CallParams.function("hello"))
			.setPaymentDefault(1000000000L);
		
		FunctionResult contractCallResult = query.execute();
		
        if (contractCallResult.getErrorMessage() != null) {
            System.out.println("error calling contract: " + contractCallResult.getErrorMessage());
            return;
        }

        System.out.println("The Smart Contract returned: " + contractCallResult.getString(0));
	}
	
	private static void introduceYourself(Client client, String smartContractId, String yourName) throws HederaNetworkException, HederaException {
		// Let's change your name
		ContractExecuteTransaction executeTx = new ContractExecuteTransaction(client)
				.setContractId(ContractId.fromString(smartContractId))
				.setGas(30000)
				.setFunctionParameters(CallParams.function("register").addString(yourName));

		TransactionReceipt receipt = executeTx.executeForReceipt();
		System.out.println("error calling contract: " + receipt.getStatus());

		System.out.println("You introduced yourself successfully!");
	}


}
