pragma solidity ^0.5.0;

contract HelloWorld {

  // Register user names
  mapping(address => string) usernames;
  mapping(address => bool) registered;

  // Constructor called when new contract is deployed
  constructor() public {
    usernames[msg.sender] = "Master";
    registered[msg.sender] = true;
  }

  function register(string calldata name)
    external
  {
    usernames[msg.sender] = name;
    registered[msg.sender] = true;
  }

  function hello()
    external
    view
    returns (string memory)
  {
    if (!(registered[msg.sender]))
      return "Do I know you?";

    return string(abi.encodePacked("Hey ", usernames[msg.sender], "!"));
  }

}
