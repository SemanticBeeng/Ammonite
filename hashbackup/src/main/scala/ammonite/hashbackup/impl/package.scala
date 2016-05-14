package ammonite.hashbackup

import ammonite.hashbackup.intf.MountType._
import ammonite.ops.{root, Path, RelPath}
import ammonite.hashbackup.intf.BackDestType.BackupDestVal
import ammonite.hashbackup.intf.BackupDir

import scala.Predef._
import scalaz.\/

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

  /**
    *
    */
  case class BackupSource(machine: Machine, shareDir: intf.BackupSrcDir, dirs: Seq[intf.BackupSrcDir],
                          mountType: MountTypeVal)
    extends intf.BackupSource {

    def shareName: String = {

      (if(mountType == LOCAL)
        root/shareDir.path
      else
        shareDir.path).toString

    }

    def localMountPath = impl.machinePath(BackupRoots.backupMountDirs, machine) / shareDir.path

    override def toString = {
        def sourcePaths = dirs map {d => shareName + "/" + d.path}
        s"BackupSource(machine = $machine, mountType = $mountType, sourcePaths = $sourcePaths, " +
          s"localMountPath = $localMountPath)" }
      }

  /**
    *
    */
  case class BackupDestinationDir(machine: Machine, kind: BackupDestVal, dir: BackupRemoteDestDir, mountType : MountTypeVal)
    extends intf.BackupDestinationDir {

    def path : Path  = machinePath(BackupRoots.backupDirs, machine) / dir.path

    override def shareDir: BackupDir = dir

    override def shareName: String = shareDir.path.toString.replace('/', '_')

    override def localMountPath: Path = impl.machinePath(BackupRoots.backupMountDirs, machine) / shareDir.path

    override def toString = s"BackupDestinationDir(machine = $machine, mountType = $mountType, " +
      s"localMountPath = $localMountPath)"

  }

  /**
    * Backup definition
    */
  sealed case class BackupDef(name: String,
                       source: intf.BackupSource,
                       destinations: Seq[intf.BackupDestination])
    extends intf.BackupDef {

    /**
      * Full path to the (local) "backup directory"
      */
    def localPath : Path  = machinePath(BackupRoots.backupDirs, source.machine) / name

    def mountPath : Path = machinePath(BackupRoots.backupMountDirs, source.machine) / name

    import ammonite.hashbackup.OSHandler._

    def mountSourcePaths(user: User) = {

      /*source.pathsToMount map {p =>*/ mountDirAs(source, user)/*}*/
    }

    /**
      *
      */
    def mountRemoteDestPaths(user: User): Seq[\/[Path, intf.MountError]] = {

      destinations map {d => mountDirAs(d, user)}
    }

    override def toString = s"BackupDef(name = $name; localPath = $localPath; " +
      s"source = $source; destinations = $destinations; mountPath =$mountPath)"

  }

  case class MountError(result : Int, message : String) extends intf.MountError {

  }

  case class User(name : String, UID :Int, GID : Int) extends intf.User

  val machinePath = (root: Path, machine: intf.Machine) => root / machine.name
}