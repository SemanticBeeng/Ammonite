package hashbackup

import hashbackup.impl.Machine

/**
  *
  */
object MachinesAndStorageBoxes {

  val semanticbrainex_nas1 = Machine("semanticbrainex_nas1", "192.168.100.6")
  val bigdatafierce = Machine("bigdatafierce", "192.168.100.3")
  val bigdatafierce_vm = Machine("bigdatafierce-vm", "192.168.1.100")
  val bigdatafierce2_vm = Machine("bigdatafierce2-vm", "192.168.1.200")
}
