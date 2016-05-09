package hashbackup

import ammonite.ops.{Path, RelPath}
import hashbackup.intf.BackDestType.BackupDestVal
import hashbackup.intf.BackupDestination

/**
  *
  */
package object impl {

  case class BackupSrcDir(path : RelPath)
    extends intf.BackupSrcDir {
  }

  case class BackupLocalDir(path : RelPath)
    extends intf.BackupLocalDir {

  }

  case class BackupRemoteSrcDir(path : RelPath)
    extends intf.BackupRemoteSrcDir {

  }

  case class BackupRemoteDestDir(path: RelPath)
    extends intf.BackupRemoteDestDir {

  }
  /**
    *
    */
  case class Machine(name : String, address : String)
    extends intf.Machine

  case class BackupDestinationDir(machine: Machine, kind: BackupDestVal, dir: intf.BackupRemoteDestDir)
    extends intf.BackupDestinationDir {

    def path : Path  = machineRelative(BackupRoots.backupDirs) / machine.name

    /**
      *
      */
    def machineRelative (relativeToPath: Path): Path = relativeToPath / machine.name
  }

  /**
    * Backup definition
    */
  case class BackupDef(srcMachine: intf.Machine,
                       srcDirs: Seq[intf.BackupSrcDir],
                       name: String,
                       destinations: Seq[intf.BackupDestination])
    extends intf.BackupDef {

    /**
      * Full paths to be backed-up
      */
    def srcPaths : Seq[Path] = srcDirs map (dir => machineRelative(BackupRoots.backupSourceMountDirs) / dir.path)

    /**
      * Full path to the (local) "backup directory"
      */
    def backupDirPath : Path  = machineRelative(BackupRoots.backupDirs) / name

    /**
      *
      */
    def machineRelative (relativeToPath: Path): Path = relativeToPath / srcMachine.name
  }
}
