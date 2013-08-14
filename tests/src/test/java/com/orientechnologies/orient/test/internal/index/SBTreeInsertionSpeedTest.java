package com.orientechnologies.orient.test.internal.index;

import org.testng.annotations.Test;

import com.orientechnologies.common.serialization.types.OStringSerializer;
import com.orientechnologies.common.test.SpeedTestMonoThread;
import com.orientechnologies.common.util.MersenneTwisterFast;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.OClusterPositionLong;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.index.sbtree.OLocalSBTree;
import com.orientechnologies.orient.core.storage.impl.local.OStorageLocalAbstract;

/**
 * @author Andrey Lomakin
 * @since 14.08.13
 */
public class SBTreeInsertionSpeedTest extends SpeedTestMonoThread {
  private ODatabaseDocumentTx  databaseDocumentTx;
  private OLocalSBTree<String> index;
  private MersenneTwisterFast  random = new MersenneTwisterFast();

  public SBTreeInsertionSpeedTest() {
    super(5000000);
  }

  @Override
  @Test(enabled = false)
  public void init() throws Exception {
    String buildDirectory = System.getProperty("buildDirectory", ".");
    if (buildDirectory == null)
      buildDirectory = ".";

    databaseDocumentTx = new ODatabaseDocumentTx("local:" + buildDirectory + "/uniqueHashIndexTest");
    if (databaseDocumentTx.exists()) {
      databaseDocumentTx.open("admin", "admin");
      databaseDocumentTx.drop();
    }

    databaseDocumentTx.create();

    index = new OLocalSBTree<String>(".sbt");
    index.create("uniqueSBTreeIndexTest", OStringSerializer.INSTANCE, (OStorageLocalAbstract) databaseDocumentTx.getStorage());
  }

  @Override
  @Test(enabled = false)
  public void cycle() throws Exception {
    String key = "bsadfasfas" + random.nextInt();
    index.get(key);
    index.put(key, new ORecordId(0, new OClusterPositionLong(0)));
  }

  @Override
  public void deinit() throws Exception {
    index.delete();
    databaseDocumentTx.close();
  }
}
