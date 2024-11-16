package com.utilitytoolbox.qrscanner.barcodescanner.usecase

import android.graphics.Bitmap
import android.graphics.Color
import com.utilitytoolbox.qrscanner.barcodescanner.model.MineBarCode
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.reactivex.Single

object BarcodeGeneratorImage {
    private val encoder = BarcodeEncoder()
    private val writer = MultiFormatWriter()

    fun generateBitmapAsync(
        MineBarCode: MineBarCode,
        width: Int,
        height: Int,
        margin: Int = 0,
        codeColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Single<Bitmap> {
        return Single.create { emitter ->
            try {
                emitter.onSuccess(generateBitmap(MineBarCode, width, height, margin, codeColor, backgroundColor))
            } catch (ex: Exception) {
                MainLogger.log(ex)
                emitter.onError(ex)
            }
        }
    }

    fun generateBitmap(
        MineBarCode: MineBarCode,
        width: Int,
        height: Int,
        margin: Int = 0,
        codeColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Bitmap {
        try {
            val matrix = encoder.encode(
                MineBarCode.text,
                MineBarCode.format,
                width,
                height,
                createHints(MineBarCode.correctionLevel, margin)
            )
            return createBitmap(matrix, codeColor, backgroundColor)
        } catch (ex: Exception) {
            throw Exception("Unable to generate barcode image, ${MineBarCode.format}, ${MineBarCode.text}", ex)
        }
    }




    private fun createHints(errorCorrectionLevel: String?, margin: Int): Map<EncodeHintType, Any> {
        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "utf-8",
            EncodeHintType.MARGIN to margin
        )

        if (errorCorrectionLevel != null) {
            hints.plus(EncodeHintType.ERROR_CORRECTION to errorCorrectionLevel)
        }

        return hints
    }

    private fun createBitmap(matrix: BitMatrix, codeColor: Int, backgroundColor: Int): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (matrix[x, y]) codeColor else backgroundColor
            }
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }
}