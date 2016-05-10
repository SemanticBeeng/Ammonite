package ammonite.hashbackup

import ammonite.ops._
import utest._
/**
  *
  */
class BackupSetupTests  extends TestSuite {


    val tests = TestSuite {
      println("UnitTests")

      'transpose {
        assert(Backups.semanticbrainex_nas1_destBackup.path == root/'mnt)

      }
    }
}



