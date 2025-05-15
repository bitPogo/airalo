package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.airalo.example.offer.R
import com.airalo.example.offer.domain.entity.Validity
import com.airalo.example.offer.domain.entity.Volume
import com.airalo.example.offer.presentation.ui.atom.DataIcon
import com.airalo.example.offer.presentation.ui.atom.OfferDetailLabel
import com.airalo.example.offer.presentation.ui.atom.OfferDetailValue
import com.airalo.example.offer.presentation.ui.atom.OfferDetailsValueSlot
import com.airalo.example.offer.presentation.ui.atom.SHorizontalSpace

@Composable
private fun ValueBox(
    accessibilityLabel: String,
    label: String,
    value: String,
) {
    Box(
        modifier = Modifier.semantics {
            contentDescription = accessibilityLabel
        },
    ) {
        OfferDetailsValueSlot(
            left = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DataIcon()
                    SHorizontalSpace()
                    OfferDetailLabel(label, null)
                }
            },
            right = {
                OfferDetailValue(value, null)
            },
        )
    }
}

@Composable
fun DataVolume(volume: Volume) =
    ValueBox(
        accessibilityLabel = stringResource(R.string.data_accessibility, volume.volume),
        label = stringResource(R.string.data_label),
        value = volume.volume,
    )

@Composable
fun ValidityDuration(validity: Validity) =
    ValueBox(
        accessibilityLabel = stringResource(R.string.validity_accessibility, validity.validity),
        label = stringResource(R.string.validity_label),
        value = validity.validity,
    )
