package ru.lrp.jacarta.certificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ru.lrp.jacarta.tlv.BMPStringTLV;
import ru.lrp.jacarta.tlv.BerTLV;
import ru.lrp.jacarta.tlv.BitStringTLV;
import ru.lrp.jacarta.tlv.EndOfContentTLV;
import ru.lrp.jacarta.tlv.IntegerTLV;
import ru.lrp.jacarta.tlv.NullTLV;
import ru.lrp.jacarta.tlv.ObjectIdentifierTLV;
import ru.lrp.jacarta.tlv.OctetStringTLV;
import ru.lrp.jacarta.tlv.SequenceTLV;
import ru.lrp.jacarta.tlv.SetTLV;
import ru.lrp.jacarta.tlv.TLVCreatingException;
import ru.lrp.jacarta.tlv.Tag;
import ru.lrp.jacarta.tlv.TagClass;
import ru.lrp.jacarta.tlv.TlvUtils;
import ru.lrp.jacarta.utils.BU;
import ru.lrp.jacarta.utils.OIDUtils;
import sun.misc.BASE64Encoder;

/**
 *
 * @author lrp
 */
public class CerificateRequest {

    private  static final Map<String, String> subjectOID;
                
    static {
        subjectOID = new HashMap<String, String>();
        subjectOID.put("СНИЛС", "1.2.643.100.3");//PrintableString
        subjectOID.put("ОГРН", "1.2.643.100.1");
        subjectOID.put("ИНН", "1.2.643.3.131.1.1");
        subjectOID.put("E", "1.2.840.113549.1.9.1"); // IA5String
        subjectOID.put("C", "2.5.4.6"); // PrintableString RU
        subjectOID.put("S", "2.5.4.8"); //    BMPString   
        subjectOID.put("L", "2.5.4.7"); 
        subjectOID.put("O", "2.5.4.10");  
        subjectOID.put("OU", "2.5.4.11");  
        subjectOID.put("CN", "2.5.4.3");  //PrintableString
        subjectOID.put("STREET", "2.5.4.9");  //BMPString
        subjectOID.put("T", "2.5.4.12");  //BMPString
        subjectOID.put("G", "2.5.4.42");  //BMPString
        subjectOID.put("SN", "2.5.4.4");  //BMPString
        subjectOID.put("ОГРНИП", "1.2.643.100.5");  //BMPString
    }
    
    public static class Builder {
    
        private final int version;
        
        private final Signer signer;
        
        private final String toolName;
        
        private final PublicKeyInfo keyInfo;
        
        
    
        private final Map<String, String> subjects;
        
        private final List<String> extendedKeyUsage;
        
        private int keyUsage;

        public Builder(int version, Signer signer, PublicKeyInfo keyInfo, String toolName) {
            this.version = version;
            this.signer = signer;
            this.toolName = toolName;
            this.keyInfo = keyInfo;
            extendedKeyUsage = new ArrayList<String>();
            subjects = new LinkedHashMap<String, String>();
        }
        
        public Builder addSubject(String name, String value){
            String oid = subjectOID.get(name);
            if(oid != null) {
                subjects.put(oid, value);
            }
            return this;
        }
        
        public Builder addKeyUsage(KeyUsage keyUsage){
            this.keyUsage |= keyUsage.getValue();
            return this;
        }
        
        public Builder addExtendedKeyUsage(String oid){
            byte[] oidArray = OIDUtils.oidStringToByteArray(oid);
            if (oidArray != null) {
                extendedKeyUsage.add(oid);
            }
            return this;
        }
        
        public CerificateRequest build() throws TLVCreatingException {
            
            // Версия
            IntegerTLV vers = new IntegerTLV(version);
            
            // Subject
            int i = 0;
            BerTLV[] subjectArray = new BerTLV[subjects.size()];
            for (Map.Entry<String, String> entry : subjects.entrySet()) {
                ObjectIdentifierTLV objectId = new ObjectIdentifierTLV(entry.getKey());
                BMPStringTLV value = new BMPStringTLV(entry.getValue());
                subjectArray[i++] = new SetTLV(new SequenceTLV(objectId, value));
            }
            SequenceTLV  subject  = new SequenceTLV(subjectArray);
            
            
            // subjectPKInfo
            ObjectIdentifierTLV algorithm = new ObjectIdentifierTLV(keyInfo.getAlgorithmOID());
            List<String> algParams = keyInfo.getAlgorithmParams();
            ObjectIdentifierTLV[] algParamsArray = new ObjectIdentifierTLV[algParams.size()];
            for (int j = 0; j < algParams.size(); j++) {
                String oid = algParams.get(j);
                algParamsArray[j] = new ObjectIdentifierTLV(oid);
            }
            SequenceTLV keyParams = new SequenceTLV(algorithm, new SequenceTLV(algParamsArray));
            BitStringTLV key = new BitStringTLV(keyInfo.getPublicKey());
            SequenceTLV subjectPKInfo  = new SequenceTLV(keyParams, key);

            // attributes
            ObjectIdentifierTLV keyUsageOID = new ObjectIdentifierTLV("2.5.29.15");
            System.out.println("keyUsage: " + keyUsage);
            byte[] usage = TlvUtils.integerToByteArray(keyUsage);
            System.out.println("keyUsage: " + BU.toHexString(usage));
            BerTLV keyUsageValue = new BerTLV(Tag.OCTET_STRING, new BitStringTLV(usage).toByteArray());
            SequenceTLV keyUsageTlv = new SequenceTLV(keyUsageOID, keyUsageValue);
            
            ObjectIdentifierTLV keyExUsageOID = new ObjectIdentifierTLV("2.5.29.37");
            ObjectIdentifierTLV[] exParams = new ObjectIdentifierTLV[extendedKeyUsage.size()];
            for (int j = 0; j < extendedKeyUsage.size(); j++) {
                String oid = extendedKeyUsage.get(j);
                exParams[j] = new ObjectIdentifierTLV(oid);
            }
            String srrValue = new String(new SequenceTLV(exParams).toByteArray());
            OctetStringTLV keyExUsageValue = new OctetStringTLV(srrValue);
            SequenceTLV keyExUsageTlv = new SequenceTLV(keyExUsageOID, keyExUsageValue);
            
            
            SetTLV extensionParams;
            if (toolName != null) {
                ObjectIdentifierTLV objectId = new ObjectIdentifierTLV("1.2.643.100.111");
                OctetStringTLV value = new OctetStringTLV(toolName);
                BerTLV toolNameTlv = new SequenceTLV(objectId, value);
                extensionParams = new SetTLV(new SequenceTLV(keyUsageTlv, keyExUsageTlv,toolNameTlv));
            } else {
                extensionParams = new SetTLV(new SequenceTLV(keyUsageTlv, keyExUsageTlv));
            }
            
            //PKCS#9 ExtensionRequest; 
            ObjectIdentifierTLV extensionRequest = new ObjectIdentifierTLV("1.2.840.113549.1.9.14");
            EndOfContentTLV attributes = new EndOfContentTLV(new SequenceTLV(extensionRequest, extensionParams));
            attributes.setClass(TagClass.CONTEXT_ORIENTED);
            
            
            // собираем воедино
            SequenceTLV requestInfo = new SequenceTLV(vers,  subject , subjectPKInfo, attributes);
            SequenceTLV request = new SequenceTLV(
                    requestInfo,
                    new SequenceTLV(new ObjectIdentifierTLV(signer.getAlgorithm()), new NullTLV()),
                    new BitStringTLV(signer.sign(requestInfo.toByteArray()))
            );
            return new CerificateRequest(request.toByteArray());
        }
    
    }

    private final byte[] data;
    
    public CerificateRequest(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
    
    public String getBAse64Data() {
        return new BASE64Encoder().encode(data);
    }
    
    
    
}
