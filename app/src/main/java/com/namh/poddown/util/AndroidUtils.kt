package com.humpbackwhale.spike.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.telephony.TelephonyManager
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import kotlin.reflect.KClass


/**
 * Created by namh on 2015-10-16.
 */


object SpkTel{


    /**
     * This retrieves the MSISDN number, but you should be careful
     * because it is not stored in SIM
     *
     * @see : http://i5on9i.blogspot.kr/2015/10/blog-post_70.html
     */

    fun getOwnNumber(ctx: Context): String{
        val tMgr = ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var phoneNumber: String? = tMgr.line1Number
        if (phoneNumber == null ){
            phoneNumber = ""
        }
        return phoneNumber

    }

    fun getNamePhoneNumber(context: Context, contactUri: Uri?): Pair<String, String>{
        val personUri = contactUri

        val (id, name) = _getIdName(context, contactUri)
        var phoneNumber = _getPhoneNumber(context, id)

        return Pair(name, phoneNumber)

    }


    fun _getIdName(context: Context, uri: Uri?): Pair<String, String>{
        var cur: Cursor? = null
        var id = ""
        var name = ""
        try{
            cur = context.contentResolver.query(
                    uri,
                    arrayOf(ContactsContract.Contacts._ID,
                            ContactsContract.PhoneLookup.DISPLAY_NAME),
                    null, null, null )



            if( cur.moveToFirst() ) {
                id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                name = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
            }
        }finally{
            cur?.close()
        }

        return Pair(id, name)

    }



    fun _getPhoneNumber(context: Context, id: String): String{
        var phoneNumber: String = ""
        var phoneCursor: Cursor? = null
        try{
            phoneCursor = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf( ContactsContract.CommonDataKinds.Phone.NUMBER ),
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ",
                    arrayOf(id), null)
            if (phoneCursor.moveToFirst()) {
                phoneNumber = phoneCursor.getString(
                        phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }

        }finally{
            phoneCursor?.close()
        }


        return phoneNumber


    }


}




object SpkKeyboard{
    fun show(context: Context, targetView: EditText){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(targetView, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * It should be called before dialog.show()
     */
    fun show(dialog: AlertDialog?){
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}


object SpkView{


    fun getClickableString(text: String, onClick: (view: View?)->Unit): SpannableString {
        val spannable = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View?) {
                onClick(view)
            }
        }
        spannable.setSpan(clickableSpan, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable

    }

    /**
     * Add the clickable span text and set movementMethod of TextView
     *
     * If your movementMethod has been set you'd better not to use this
     */

    fun appendClickableText(textView: TextView, text: String, onClick: (view: View?)->Unit) {
        val ckstr = getClickableString(text, onClick)
        textView.append(ckstr)
        textView.movementMethod = LinkMovementMethod.getInstance()

    }

    fun setTextWatcher(inputView: EditText, afterChanged: (s: Editable?)->Unit = {s->},
                       beforeChanged: (s: CharSequence?, start: Int, count: Int, after: Int)->Unit = {s, start, count, after -> }) {


        inputView.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                beforeChanged(s, start, count, after)
            }

            override fun afterTextChanged(s: Editable?) {
                afterChanged(s)
            }
        })
    }

    fun simpleInputFilter(accept:(newText: String)-> Boolean): InputFilter{
        return InputFilter f@{ src, s, e,
                                 dest, ds, de ->
            // Remove the string out of destination that is to be replaced
            val dstr = dest.toString()
            val new = dstr.substring(0, ds) + src.toString().substring(s, e) + dstr.substring(de, dstr.length)

            return@f if(accept(new)) null else ""
        }
    }



}

object App {
    fun exit(){
        android.os.Process.killProcess(android.os.Process.myPid())
    }

}



object SpkActivity{


    fun startActivity(activity: Activity, cls: KClass<out Any>){
        val intent = Intent(activity, cls.java)
        activity.startActivity(intent)
    }


    fun startActivityForResult(activity: Activity, cls: KClass<out Any>, key: Int){
        val intent = Intent(activity, cls.java)
        activity.startActivityForResult(intent, key)
    }


    fun startActivityWithBundle(activity: Activity, cls: KClass<out Any>, bundle: Bundle){
        val intent = Intent(activity, cls.java)
        intent.putExtras(bundle)

        activity.startActivity(intent)
    }

    fun startActivityForResultWithBundle(activity: Activity, cls: KClass<out Any>,
                                         key: Int, bundle: Bundle){
        val intent = Intent(activity, cls.java)
        intent.putExtras(bundle)

        activity.startActivityForResult(intent, key)
    }

    fun startActivityEmptyStack(activity: Activity, cls: KClass<out Any>){
        val intent = Intent(activity, cls.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
    }


//    fun setActionBarNav(activity: AppCompatActivity, toolbar: Toolbar,
//                            onNavClick:(view: View?)-> Unit = { activity.finish() }) {
//        activity.setSupportActionBar(toolbar)
//
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)  // back arrow
//        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                onNavClick(v)
//            }
//
//        })
//    }


    fun setActionBarNoNav(activity: AppCompatActivity, toolbar: Toolbar) {
        activity.setSupportActionBar(toolbar)

    }

//    fun startSignUpActiWithExitFlag(activity: Activity){
//
//        val intent = Intent(activity, SignUpActivity::class.java)
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.putExtra(IntentParam.EXIT, true)
//        activity.startActivity(intent)
//    }


}

object SpkFragment{
    fun replace(activity: FragmentActivity, containerId: Int, frag: Fragment){
        val t = activity.supportFragmentManager.beginTransaction();
        // @see http://stackoverflow.com/a/7892524
        t.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                android.R.anim.fade_in,android.R.anim.fade_out);
        t.replace(containerId, frag);
        t.commit();
    }
    fun add(activity: FragmentActivity, containerId: Int, frag: Fragment){
        val t = activity.supportFragmentManager.beginTransaction();
        t.add(containerId, frag);
        t.commit();
    }

    fun startActivityForResult(frag: Fragment, cls: KClass<out Any>, key: Int){
        val intent = Intent(frag.context, cls.java)
        frag.startActivityForResult(intent, key)
    }

    fun startActivityForResultWithBundle(frag: Fragment, cls: KClass<out Any>, bundle: Bundle, key: Int){
        val intent = Intent(frag.context, cls.java)
        intent.putExtras(bundle)

        frag.startActivityForResult(intent, key)

    }
}

object AndroidDialog{


    fun show(context: Context, title: String = "", message: String = "",
             yesText: String = "OK", noText: String = "Cancel",
           onYesClick: (dialog: DialogInterface?, which: Int)-> Unit = {d,h -> },
           onNoClick: (dialog: DialogInterface?, which: Int)-> Unit = {d,h -> }){

        val alertDialog = AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, yesText,
            object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    onYesClick(dialog, which)
                }
            });
        if(noText != ""){
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, noText,
                object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        onNoClick(dialog, which)
                    }
                });
        }

        alertDialog.show();
    }


    /**
     * @return
     *  AlertDialog : the Dialog which is created using {@param resId}
     *  View : view which is set to {@return AlertDialog}
     *
     */
    fun create(activity: Activity, res: Any,
               okId: Int,
               onOkClick:(dialog: AlertDialog, v: View)->Unit,
               cancelId: Int,
               onCancelClick:(dialog: AlertDialog, v: View)->Unit): Pair<AlertDialog, View>{


        fun getView(res: Any): View?{
            var view: View? = null
            if(res is View){
                view = res
            }else if(res is Int){
                view = activity.layoutInflater.inflate(res, null)
            }
            return view
        }


        /**
         * Start here...
         */
        var view = getView(res) ?: throw ClassCastException("res type must be View or Int")

        val builder = AlertDialog.Builder(activity)
        builder.setView(view)

        val dialog = builder.create()


        // @Notice : dialog.findViewById() works only after dialog.show() is called
        val okButton = view.findViewById(okId)
        okButton.setOnClickListener{v->
            onOkClick(dialog, v)
        }


        val cancelButton = view.findViewById(cancelId)
        cancelButton.setOnClickListener{ v->
            onCancelClick(dialog, v)
        }

        return Pair(dialog, view)
    }


    fun progress(context: Context,
                 style: Int = ProgressDialog.STYLE_SPINNER,
                 message: String = "",
                 indeterminate: Boolean = true): ProgressDialog{
        val progressDialog = ProgressDialog(context)

        progressDialog.setMessage(message)
        progressDialog.setProgressStyle(style)
        progressDialog.isIndeterminate = indeterminate  // true : infinite

        return progressDialog
    }
}

object SpkAnimation{
    fun run(context: Context, v: View, aniId: Int){
        val ani = AnimationUtils.loadAnimation(context, aniId)
        v.startAnimation(ani)
    }
}