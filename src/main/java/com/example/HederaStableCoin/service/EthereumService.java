package com.example.HederaStableCoin.service;

import java.math.BigDecimal;

import com.example.HederaStableCoin.model.dto.EthTransactionResponseDTO;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

@Service
public class EthereumService {

    private final Web3j web3j;

    public EthereumService() {
        // Replace with your Ethereum node endpoint (Infura, Alchemy, local, etc.)
        this.web3j = Web3j.build(new HttpService("https://sepolia.infura.io/v3/148c56a8bd6649b0a15abc7d4d0a3607"));
    }

    public EthTransactionResponseDTO sendEthWithReceipt(String fromPrivateKey, String toAddress, BigDecimal amountInEther) throws Exception {
        Credentials credentials = Credentials.create(fromPrivateKey);
        TransactionReceipt receipt = Transfer.sendFunds(
                web3j,
                credentials,
                toAddress,
                amountInEther,
                Convert.Unit.ETHER
        ).send();

        EthTransactionResponseDTO etherResponse = new EthTransactionResponseDTO();
        etherResponse.setTransactionHash(receipt.getTransactionHash());
        etherResponse.setStatus(receipt.getStatus());
        etherResponse.setBlockHash(receipt.getBlockHash());
        etherResponse.setBlockNumber(receipt.getBlockNumber().toString());
        etherResponse.setFrom(receipt.getFrom());
        etherResponse.setTo(receipt.getTo());
        etherResponse.setGasUsed(receipt.getGasUsed().toString());
        return etherResponse;
    }

    public String getClientVersion() throws Exception {
        Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
        return clientVersion.getWeb3ClientVersion();
    }


    public String generateAddress() throws Exception {
        ECKeyPair keyPair = Keys.createEcKeyPair();
        String address = "0x" + Keys.getAddress(keyPair.getPublicKey());
        String privateKey = keyPair.getPrivateKey().toString(16);
        return "Address: " + address + "\nPrivate Key: " + privateKey;
    }

    // Get transaction details by hash
    public String getTransactionByHash(String txHash) throws Exception {
        EthTransaction transactionResponse = web3j.ethGetTransactionByHash(txHash).send();
        if (transactionResponse.getTransaction().isPresent()) {
            // Fetch the receipt
            var receiptResponse = web3j.ethGetTransactionReceipt(txHash).send();
            if (receiptResponse.getTransactionReceipt().isPresent()) {
                return receiptResponse.getTransactionReceipt().get().toString();
            } else {
                return "Transaction found, but receipt not yet available (pending or failed).";
            }
        } else {
            return "Transaction not found";
        }
    }

    public String getBalance(String address) throws Exception {
        // Returns balance in Ether as a String
        var ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        return Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER).toPlainString();
    }

    public boolean isValidAddress(String address) {
        // Checks if the address is a valid Ethereum address
        return Numeric.containsHexPrefix(address)
                && address.length() == 42
                && Keys.toChecksumAddress(address).equalsIgnoreCase(address);
    }
}