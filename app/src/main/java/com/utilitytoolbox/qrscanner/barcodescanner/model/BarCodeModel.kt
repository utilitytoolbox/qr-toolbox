package com.utilitytoolbox.qrscanner.barcodescanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.zxing.BarcodeFormat
import com.utilitytoolbox.qrscanner.barcodescanner.model.schema.BarcodeSchema
import com.utilitytoolbox.qrscanner.barcodescanner.usecase.BarcodeDatabaseTypeConverter
import java.io.Serializable

@Entity(tableName = "qr_codes")
@TypeConverters(BarcodeDatabaseTypeConverter::class)
data class MineBarCode(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String? = null,
    val text: String,
    val formattedText: String,
    val format: BarcodeFormat,
    val schema: BarcodeSchema,
    val datetime: Long,
    val isGenerated: Boolean = false,
    var isFavorite: Boolean = false,
    val correctionLevel: String? = null,
    var isScanned: Boolean = true,
    var isHeader: Boolean = false
) : Serializable