package com.toxicbakery.kfinstatemachine.xml

import com.toxicbakery.kfinstatemachine.Transition

data class FiniteXmlTransition(
        override val event: String
) : Transition