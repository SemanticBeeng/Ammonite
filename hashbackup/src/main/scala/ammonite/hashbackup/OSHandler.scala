package ammonite.hashbackup

import ammonite.ops._
import ammonite.ops.ImplicitWd._
import ammonite.hashbackup.impl.User
import ammonite.hashbackup.intf._
import ammonite.hashbackup.intf.MountType._
import ammonite.ops.Path

/**
  *
  */
object OSHandler {

  def mountDirAs(/*machine: Machine, */ mountable: Mountable, user : User) : Either[Path, MountError] = {

    mountable.mountType match  {

      case LOCAL =>
          Left(root / mountable.shareDir.path)

      case SSHFS =>
        val shareName: String = mountable.shareName
        val localPath = mountable.localMountPath.toString

        val cmd = s"sshfs -o uid=${user.UID},gid=${user.GID},reconnect " +
                  s"${mountable.machine.address}:$shareName $localPath"
        println(s"Executing $cmd")

        val res = %%(cmd)
        if (res.exitCode == 0) Left(mountable.localMountPath)
        else Right(new impl.MountError(res.exitCode, res.out.string))


      case CIFS =>
        val shareName: String = mountable.shareName
        val localPath = mountable.localMountPath.toString

        val cmd = s"mount -t cifs -o user=${user.name} " +
                  s"//${mountable.machine.address}/$shareName $localPath"
        println(s"Executing $cmd")

        val res = %%(cmd)
        if (res.exitCode == 0) Left(mountable.localMountPath)
        else Right(new impl.MountError(res.exitCode, res.out.string))

      case m =>
        Right(new impl.MountError(-1, "Unknown mount type" + m))
    }

  }

  //val f: (Path, User, MountTypeVal) => Either[Path, MountError] = mountDirAs

  //  val mountDirAs //: (User, MountTypeVal) => Either[Path, MountStatus] =
  //    = (user : User, mountType : MountTypeVal) => /*Either[Path, MountStatus] =*/ {
  //    Left(new Path(""))
  //  }


}
