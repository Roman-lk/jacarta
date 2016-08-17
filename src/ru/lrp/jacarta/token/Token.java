package ru.lrp.jacarta.token;

import ru.lrp.jacarta.token.key.KeyPair;
import ru.lrp.jacarta.token.key.KeyPair.PrivateKey;
import ru.lrp.jacarta.token.key.KeyPair.PublicKey;

/**
 * 
 *
 * @author lrp
 */
public interface Token {
   
    public void login(String pin) throws TokenOperationException;

  
    public byte[] getRandomData(int len) throws TokenOperationException;
  
    public KeyPair generateKeyPair() throws TokenOperationException;
  
    public byte[] hash(byte[] data) throws TokenOperationException;

    public byte[] sign(PrivateKey key, byte[] data) throws TokenOperationException;

    public boolean verify(PublicKey key, byte[] data, byte[] sign) throws TokenOperationException;
  
    public void loadCert(byte[] certificate) throws TokenOperationException;


}
