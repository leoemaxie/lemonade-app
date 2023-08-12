package com.leo.lemonadeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

public class MainActivity : AppCompatActivity() {
    
    /**
     * DO NOT ALTER ANY VARIABLE OR VALUE NAMES OR THEIR INITIAL VALUES.
     *
     * Anything labeled var instead of val is expected to be changed in the functions but DO NOT
     * alter their initial values declared here, this could cause the app to not function properly.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null
     
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        
        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener { clickLemonImage() }
        lemonImage!!.setOnLongClickListener { showSnackbar() }
    }

    /**
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicking will elicit a different response depending on the state.
     * This method determines the state and proceeds with the correct action.
     */
    private fun clickLemonImage() {
        // Changes the state of the lemonade after each user click
        if (lemonadeState == SELECT) {
            lemonadeState = SQUEEZE
            lemonSize = lemonTree.pick()
            squeezeCount = 0
        }
        else if (lemonadeState == SQUEEZE) {
            if (lemonSize > 0) {
                lemonSize -= 1
                squeezeCount += 1
            }
            else {
                lemonadeState = DRINK
                lemonSize = -1
            }
        }
        else if (lemonadeState == DRINK) lemonadeState = RESTART
        else lemonadeState = SELECT
        
        setViewElements()
    }
    
    /**
     * Set up the view elements according to the state.
     */
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        
        // Select the corresponding text according to the lemonadeState from the
        // string resource file.
        var text = when (lemonadeState) {
            SELECT -> R.string.lemon_select
            SQUEEZE -> R.string.lemon_squeeze
            DRINK -> R.string.lemon_drink
            else -> R.string.lemon_empty_glass
        }
        // Change the text of textAction to the value of the  text variable
        textAction.text = getString(text)
        
        // Select the corresponding image according to the lemonadeState from the drawable 
        // resources file.
        var imageResource = when (lemonadeState) {
            SELECT -> R.drawable.lemon_tree
            SQUEEZE -> R.drawable.lemon_squeeze
            DRINK -> R.drawable.lemon_drink
            else -> R.drawable.lemon_restart
        }
        // Set the lemonadeImage to the drawable resource file in the imageResource variable
        lemonImage?.setImageResource(imageResource)
    }

    /**
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
               return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
            Snackbar.make(
                findViewById(R.id.constraint_Layout),
                squeezeText,
                Snackbar.LENGTH_SHORT
            ).show()
        return true
    }
}

/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 */
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}