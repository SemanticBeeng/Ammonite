package ammonite.hashbackup

import ammonite.hashbackup.intf.MountType._
import ammonite.ops.{Path, RelPath}
import ammonite.hashbackup.intf.BackDestType.BackupDestVal
import ammonite.hashbackup.intf.BackupDir

import scala.Predef._

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

//      if(shareDir.isInstanceOf[BackupLocalDir])
//      assert(shareDir.path.segments.length == 1)
      shareDir.path.toString
    }

    def localMountPath = impl.machinePath(BackupRoots.backupMountDirs, machine) / shareDir.path


//    def pathsToMount : Seq[Path] = {
//
//      if (mountType == LOCAL)
//        Seq.empty[Path]
//      else
//        Seq(machinePath(BackupRoots.backupSourceMountDirs, machine) / shareDir.path)
//        //dirs map (dir => machinePath(BackupRoots.backupSourceMountDirs, machine) / dir.path)
//    }
  }

  /**
    *
    */
  case class BackupDestinationDir(machine: Machine, kind: BackupDestVal, dir: BackupRemoteDestDir, mountType : MountTypeVal)
    extends intf.BackupDestinationDir {

    def path : Path  = machinePath(BackupRoots.backupDirs, machine) / dir.path

//    override def pathsToMount: Seq[Path] = List(path).toSeq
    override def shareDir: BackupDir = dir

    override def shareName: String = shareDir.path.toString

    override def localMountPath: Path = impl.machinePath(BackupRoots.backupMountDirs, machine) / shareDir.path
  }

  /**
    * Backup definition
    */
  case class BackupDef(name: String,
                       source: intf.BackupSource,
                       destinations: Seq[intf.BackupDestination])
    extends intf.BackupDef {

    /**
      * Full paths to be backed-up
      */
    //def srcPaths : Seq[Path] = source.pathsToMount

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
    def mountRemoteDestPaths(user: User): Seq[Either[Path, intf.MountError]] = {

      //val a = Seq.empty[intf.BackupDestination]

//      for(d <- destinations if d.isInstanceOf[BackupDestinationDir])
//        yield /*d.asInstanceOf[BackupDestinationDir]*/
//          /*map {p => */mountDirAs(d, user)/*}*/

      destinations map {d => mountDirAs(d, user)}
    }

  }

  case class MountError(result : Int, message : String) extends intf.MountError {

  }

  case class User(name : String, UID :Int, GID : Int) extends intf.User

  val machinePath = (root: Path, machine: intf.Machine) => root / machine.name

}
