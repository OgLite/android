package com.orange.og_lite.Util

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView

/**
 * The `Util_CustomTextWatcher` class is used to add a TextWatcher to the
 * list of those whose methods are called whenever this TextView's text changes.
 *
 * @author IndexCqq
 * @version 1.00.00, 11 May 2015
 */
class Util_CustomTextWatcher
/**
 * Creates an instance of `Util_CustomTextWatcher`.
 *
 * @param editText
 * the editText to edit text.
 */
(
    /**
         * The editText to edit text.
         */
        private val mEditText: EditText, private val count: Int, private val image:TextView, private val imageInt_true:Int,private val imageInt_fail:Int
) : TextWatcher {

    private var mFormat: Boolean = false

    private var mInvalid: Boolean = false

    private var mSelection: Int = 0
    private var mLastText: String? = null

    init {
        mFormat = false
        mInvalid = false
        mLastText = ""
        this.mEditText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun beforeTextChanged(charSequence: CharSequence, start: Int,
                                   count: Int, after: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int,
                               count: Int) {

        try {

            var temp = charSequence.toString()

            // Set selection.
            if (mLastText == temp) {
                if (mInvalid) {
                    mSelection -= 1
                } else {
                    if (mSelection >= 1 && temp.length > mSelection - 1
                            && temp[mSelection - 1] == ' ') {
                        mSelection += 1
                    }
                }
                val length = mLastText!!.length
                if (mSelection > length) {

                    mEditText.setSelection(length)
                } else {

                    mEditText.setSelection(mSelection)
                }
                mFormat = false
                mInvalid = false
                return
            }

            mFormat = true
            mSelection = start

            // Delete operation.
            if (count == 0) {
                if (mSelection >= 1 && temp.length > mSelection - 1
                        && temp[mSelection - 1] == ' ') {
                    mSelection -= 1
                }

                return
            }

            // Input operation.
            mSelection += count
            val lastChar = temp.substring(start, start + count)
                    .toCharArray()
            val mid = lastChar[0].toInt()
            if (mid >= 48 && mid <= 57) {
                /* 1-9. */
            } else if (mid >= 65 && mid <= 70) {
                /* A-F. */
            } else if (mid >= 97 && mid <= 102) {
                /* a-f. */
            } else {
                /* Invalid input. */
                mInvalid = true
                temp = temp.substring(0, start) + temp.substring(start + count, temp.length)
                mEditText.setText(temp)
                return
            }

        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }

    }

    override fun afterTextChanged(editable: Editable) {

        try {
            /* Format input. */
            if (mFormat) {
                val text = StringBuilder()
                text.append(editable.toString().replace(" ", ""))
                mLastText = text.toString()
                mEditText.setText(text)
            }
            if (mEditText.text.toString().length == count) {
                image.setBackgroundResource(imageInt_true)
                //mEditText.setBackgroundResource(R.mipmap.icon_input_box_write)
            } else {
                image.setBackgroundResource(imageInt_fail)
                //mEditText.setBackgroundResource(R.mipmap.icon_input_box_locked)
            }
//            if (frag.rootview.Lft.text.length >= 6&&frag.rootview.Rrt.text.length >= 6&&frag.rootview.Rft.text.length >= 6&&frag.rootview.Lrt.text.length >= 6) {
//                frag.rootview.condition.text = frag.resources.getString(R.string.Please_press_Sending_data)
//            } else {
//                frag.rootview.condition.text = frag.resources.getString(R.string.Choose_the_tire_position_and_enter_new_sensor_ID_number)
//            }
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }

    }

    companion object {

        private val TAG = "Util_CustomTextWatcher"
    }

}