package com.hieuld.datn.mathsolved.ui.cropper

import android.net.Uri

data class CropImageContractOptions(
  val uri: Uri?,
  val cropImageOptions: CropImageOptions,
)
