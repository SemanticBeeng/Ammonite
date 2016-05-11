package ammonite.hashbackup

import ammonite.ops._
import ammonite.hashbackup.impl.User
import ammonite.hashbackup.intf.{Machine, BackupDir, MountError}
import ammonite.hashbackup.intf.MountType.{CIFS, SSHFS, LOCAL, MountTypeVal}
import ammonite.ops.Path

/**
  *
  */
object OSHandler {

  def mountDirAs(machine: Machine, path : Path,  localPath : Path,  user : User, mountType : MountTypeVal) :
  Either[Path, MountError] = {

    mountType match  {

      case LOCAL =>
          Left(localPath)

      case SSHFS =>
        val cmd = s"sshfs -o uid=${user.UID},gid=${user.GID},reconnect " +
                  s"${machine.address}:${path.toString} ${localPath.toString}"
        val res = %%(cmd)
        if (res.exitCode == 0) Left(localPath)
        else Right(new impl.MountError(res.exitCode, res.out.string)))


      case CIFS =>
        val cmd = s"sshfs -o uid=${user.UID},gid=${user.GID},reconnect " +
            s"${machine.address}:${path.toString} ${localPath.toString}"
        val res = %%(cmd)
        if (res.exitCode == 0) Left(localPath)
        else Right(new impl.MountError(res.exitCode, res.out.string)))

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
