1. Start and deploy the contract

 ```
 truffle develop
 truffle(develop)> migrate
 ```

2. Then, get a reference on the contract and the list of accounts

 ```
 truffle(develop)> let instance = await HelloWorld.deployed()
 truffle(develop)> let accounts = await web3.eth.getAccounts()
 ```

3. Verify that the first account is the master, but the other accounts are not registered

 ```
 truffle(develop)> await instance.hello.call()
 'Hey Master!'
 truffle(develop)> await instance.hello.call({ from: accounts[1] })
 'Should I know you?'
 ```

4. Just introduce you to the smart contract

 ```
 truffle(develop)> instance.register("Jon Snow", { from: accounts[1] })
 { ... }
 truffle(develop)> await instance.hello.call({ from: accounts[1] })
 'Hey Jon Snow!'
 ```

5. If you are happy with the contract, you can generate the binary to import in Hedera:

 ```
 > solc --version
 solc, the solidity compiler commandline interface
 Version: 0.5.3+commit.10d17f24.Darwin.appleclang
 > solc ./contracts/HelloWorld.sol --bin --abi --optimize -o ./build/resources
 Compiler run successful. Artifact(s) can be found in directory ./build/resources.
```
