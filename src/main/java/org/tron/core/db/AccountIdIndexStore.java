package org.tron.core.db;

import com.google.protobuf.ByteString;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tron.common.utils.ByteArray;
import org.tron.core.capsule.AccountCapsule;
import org.tron.core.capsule.BytesCapsule;

//todo ： need Compatibility test
@Component
public class AccountIdIndexStore extends TronStoreWithRevoking<BytesCapsule> {

  @Autowired
  public AccountIdIndexStore(@Value("accountid-index") String dbName) {
    super(dbName);
  }

  public void put(AccountCapsule accountCapsule) {
    byte[] lowerCaseAccountId = getLowerCaseAccountId(accountCapsule.getAccountId().toByteArray());
    put(lowerCaseAccountId, new BytesCapsule(accountCapsule.getAddress().toByteArray()));
}

//
//  public byte[] get(ByteString name) {
//    BytesCapsule bytesCapsule = get(name.toByteArray());
//    if (Objects.nonNull(bytesCapsule)) {
//      return bytesCapsule.getData();
//    }
//    return null;
//  }

  @Override
  public BytesCapsule get(byte[] key) {
    byte[] lowerCaseKey = getLowerCaseAccountId(key);
    byte[] value = dbSource.getData(lowerCaseKey);
    if (ArrayUtils.isEmpty(value)) {
      return null;
    }
    return new BytesCapsule(value);
  }

  @Override
  public boolean has(byte[] key) {
    byte[] lowerCaseKey = getLowerCaseAccountId(key);
    byte[] value = dbSource.getData(lowerCaseKey);
    if (ArrayUtils.isEmpty(value)) {
      return false;
    }
    return true;
  }

  private static byte[] getLowerCaseAccountId(byte[] bsAccountId) {
    return ByteString
        .copyFromUtf8(ByteString.copyFrom(bsAccountId).toStringUtf8().toLowerCase()).toByteArray();
  }

}