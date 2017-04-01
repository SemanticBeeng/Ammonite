package ammonite.hashbackup

import ammonite.hashbackup.impl.Machine

/**
  *
  */
object MachinesAndStorageBoxes {

  // Real machines
  val bigdatafierce = Machine("bigdatafierce", "192.168.100.5")
  val bigdatafierce_hz1 = Machine("bigdatafierce-hz1", "176.9.107.211")

  // Virtual machines
  val bigdatafierce_vm = Machine("bigdatafierce-vm", "192.168.100.9")
  val bigdatafierce2_vm = Machine("bigdatafierce2-vm", "192.168.100.2")

  // Storage boxes
  val semanticbrainex_nas1 = Machine("semanticbrainex_nas1", "192.168.100.7")
  val storagebox_hz1 = Machine("storagebox_hz1", "u126308@u126308.your-storagebox.de") //@todo fixL hardcoded user id
  val storagebox_b21 = Machine("storagebox_b21", "sftree-0000.backblaze.com/b2_browse_files.htm?bucketId=c8e7e3be614383c055420a1b")

}
