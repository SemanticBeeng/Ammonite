package ammonite.hashbackup

import java.nio.file.FileSystemException
import java.nio.file.attribute.PosixFilePermission._

import ammonite.ops._
import ammonite.ops.ImplicitWd._
import ammonite.hashbackup.intf._
import ammonite.hashbackup.intf.MountType._

import scala.util.{Failure, Try}
import scalaz.\/

/**
  *
  */
object OSHandler {

  /**
    *
    */
  def ensureRootsGoodToMountUnder() = {

    def isWritableDir(info: stat): Boolean = {
      !info.isDir && info.permissions.exists(p => p == OWNER_WRITE || p == GROUP_WRITE || p == OTHERS_WRITE)
    }

    def check(d: Path): Unit = {

      val info: stat = stat! d
      if (isWritableDir(info))
        throw new FileSystemException(s"Folder ${d.toString} not suitable to use for mount")
    }

    val l = BackupRoots.all map (d => Try(check(d)).recoverWith({
      case e : FileSystemException => Failure (e)
    }))

    println(l)

  }

  /**
    *
    */
  def mountDirAs[P <: BasePath](mountable: intf.Mountable[P], user : User) : \/[Path, intf.MountError] = {

    def executeCmd(command: String): \/[Path, intf.MountError] = {

      val res: CommandResult = CommandResult(0, Seq.empty)//@todo %%(command)
      if (res.exitCode == 0)
        \/.left(mountable.localMountPath)
      else
        \/.right(new impl.MountError(res.exitCode, res.out.string))
    }

    mountable.mountType match  {

      case LOCAL =>
        \/.left(mountable.shareDir.path.asInstanceOf[Path]) //@todo fix this

      case SSHFS =>
        val shareName: String = mountable.shareName
        val localPath = mountable.localMountPath.toString

        val cmd = s"sudo sshfs -o uid=${user.UID},gid=${user.GID},reconnect " +
                  s"${mountable.machine.address}:/$shareName $localPath"
        println(s"\n>>> Executing >>> $cmd")

        executeCmd(cmd)

      case CIFS =>
        val shareName: String = mountable.shareName
        val localPath = mountable.localMountPath.toString

        val cmd = s"sudo mount -t cifs -o user=${user.name} " +
                  s"//${mountable.machine.address}/$shareName $localPath"
        println(s"Executing $cmd")

        executeCmd(cmd)

      case m =>
        \/.right(new impl.MountError(-1, "Unknown mount type" + m))

    }
  }

  /**
    *
    */
  def executeBackup(backup: BackupDef[_]) = {

    val key = "5dfe31efb14ad21c9410202d9c9e75978c70de2a3002f85da4a0db6a362f2be3"
    val cmds = List(
      s" hb init   -c ${backup.localPath}",
      s" hb audit  -c ${backup.localPath} -a",
      s" hb config -c ${backup.localPath} arc-size-limit 1gb",
      s" hb config -c ${backup.localPath} cache-size-limit 100g",
      s" hb config -c ${backup.localPath} remote-update normal",
      s" hb rekey  -c ${backup.localPath} -k $key -p ask",
      s" hb backup -D500m ${backup.sourcePaths} -c ${backup.localPath}"
    )

    cmds foreach { cmd =>
      print("\n>>> Executing >>> $cmd")
    }
  }

  //val f: (Path, User, MountTypeVal) => Either[Path, MountError] = mountDirAs

  //  val mountDirAs //: (User, MountTypeVal) => Either[Path, MountStatus] =
  //    = (user : User, mountType : MountTypeVal) => /*Either[Path, MountStatus] =*/ {
  //    Left(new Path(""))
  //  }


}
