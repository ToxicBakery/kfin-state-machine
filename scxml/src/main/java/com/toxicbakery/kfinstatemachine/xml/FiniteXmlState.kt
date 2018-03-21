package com.toxicbakery.kfinstatemachine.xml

import com.toxicbakery.kfinstatemachine.FiniteState

data class FiniteXmlState(
        override val id: String
) : FiniteState