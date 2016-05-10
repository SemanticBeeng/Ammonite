package ammonite.hashbackup

import ammonite.hashbackup.impl.User
import ammonite.hashbackup.intf.{BackupDir, MountStatus}
import ammonite.hashbackup.intf.MountType.MountTypeVal
import ammonite.ops.Path

/**
  *
  */
object OSHandler {

  def mountDirAs(path : Path,  user : User, mountType : MountTypeVal) : Either[Path, MountStatus] = {
    Left(ammonite.ops.root)
  }

  val f: (Path, User, MountTypeVal) => Either[Path, MountStatus] = mountDirAs

  //  val mountDirAs //: (User, MountTypeVal) => Either[Path, MountStatus] =
  //    = (user : User, mountType : MountTypeVal) => /*Either[Path, MountStatus] =*/ {
  //    Left(new Path(""))
  //  }


}
