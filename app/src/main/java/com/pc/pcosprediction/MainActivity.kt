package com.pc.pcosprediction

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var mlinput: MLModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
    }

    fun initializeViews(){
        sb_exercise.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {}

            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onStopTrackingTouch(seek: SeekBar) {
                Toast.makeText(
                        this@MainActivity,
                    "${seek.progress} day(s)/week",
                        Toast.LENGTH_SHORT
                    ).show()
            }

        })

        sb_eat_outside.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {}

            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onStopTrackingTouch(seek: SeekBar) {
                Toast.makeText(
                    this@MainActivity,
                    "${seek.progress} day(s)/week",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

        btn_submit.setOnClickListener {
            if(validate()) {
                val period_length = getPL()
                val cycle_length = getCL()
                val age = getAge()
                val overweight = getSpinnerValue(spn_bmi)
                val weight_gain_or_lose = getSpinnerValue(spn_weight)
                val missed_periods = getSpinnerValue(spn_missed_periods)
                val conceiving = getSpinnerValue(spn_conceiving)
                val hair_chin = hasHairGrowth(rdg_chin.checkedRadioButtonId)
                val hair_cheeks = hasHairGrowth(rdg_cheeks.checkedRadioButtonId)
                val hair_breasts = hasHairGrowth(rdg_breasts.checkedRadioButtonId)
                val hair_lips = hasHairGrowth(rdg_lips.checkedRadioButtonId)
                val hair_arms = hasHairGrowth(rdg_arms.checkedRadioButtonId)
                val hair_inner_thighs = hasHairGrowth(rdg_inner_thighs.checkedRadioButtonId)
                val acne = getSpinnerValue(spn_acne)
                val hair_loss = getSpinnerValue(spn_hair_loss)
                val dark_patches = getSpinnerValue(spn_dark_patches)
                val tired = getSpinnerValue(spn_tired)
                val mood_swings = getSpinnerValue(spn_mood_swings)
                val exercise = sb_exercise.progress
                val eat_outside = sb_eat_outside.progress
                val canned_food = getSpinnerValue(spn_canned_food)

                mlinput = MLModel(
                    period_length,
                    cycle_length,
                    age,
                    overweight,
                    weight_gain_or_lose,
                    missed_periods,
                    conceiving,
                    hair_chin,
                    hair_cheeks,
                    hair_breasts,
                    hair_lips,
                    hair_arms,
                    hair_inner_thighs,
                    acne,
                    hair_loss,
                    dark_patches,
                    tired,
                    mood_swings,
                    exercise,
                    eat_outside,
                    canned_food
                )

                networkcall()
            }
        }

    }

    fun getPL(): Int {
        val id: Int = rdg_period_length.checkedRadioButtonId
        if (id!=-1) {
            val radio: RadioButton = findViewById(id)
            when(radio.text) {
                getString(R.string.period_length_0) -> return 3
                getString(R.string.period_length_1) -> return 5
                getString(R.string.period_length_2) -> return 7
                getString(R.string.period_length_3) -> return 9
            }
        }

        return -1
    }

    fun getCL(): Int {
        val id: Int = rdg_cycle_length.checkedRadioButtonId
        if (id!=-1) {
            val radio: RadioButton = findViewById(id)
            when(radio.text) {
                getString(R.string.cycle_length_0) -> return 1
                getString(R.string.cycle_length_1) -> return 2
                getString(R.string.cycle_length_2) -> return 3
                getString(R.string.cycle_length_3) -> return 4
                getString(R.string.cycle_length_4) -> return 5
            }
        }

        return -1
    }

    fun getAge(): Int {
        val id: Int = rdg_age.checkedRadioButtonId
        if (id!=-1) {
            val radio: RadioButton = findViewById(id)
            when(radio.text) {
                getString(R.string.age_0) -> return 1
                getString(R.string.age_1) -> return 2
                getString(R.string.age_2) -> return 3
                getString(R.string.age_3) -> return 4
                getString(R.string.age_4) -> return 5
                getString(R.string.age_5) -> return 6
                getString(R.string.age_6) -> return 7
            }
        }

        return -1
    }

    fun getSpinnerValue(spn_val: Spinner): Int {
        when(spn_val.selectedItem.toString()) {
            "Yes" -> return 1
        }

        return 0
    }

    fun hasHairGrowth(id: Int): Int {
        if (id != -1) {
            val radio: RadioButton = findViewById(id)
            when (radio.text) {
                getString(R.string.normal) -> return 0
                getString(R.string.moderate) -> return 1
                getString(R.string.excessive) -> return 2
            }
        }

        return -1
    }

    fun validate(): Boolean {
        if(rdg_age.checkedRadioButtonId == -1) {
            showAlert("You need to mention your age")
            return false
        }

        if (rdg_period_length.checkedRadioButtonId == -1) {
            showAlert("You need to mention your period length")
            return false
        }

        if (rdg_cycle_length.checkedRadioButtonId == -1) {
            showAlert("You need to mention your cycle length")
            return false
        }

        if (rdg_chin.checkedRadioButtonId == -1) {
            showAlert("You need to mention hair growth on chin")
            return false
        }

        if (rdg_cheeks.checkedRadioButtonId == -1) {
            showAlert("You need to mention hair growth on cheeks")
            return false
        }

        if (rdg_breasts.checkedRadioButtonId == -1) {
            showAlert("You need to mention hair growth between breasts")
            return false
        }

        if (rdg_lips.checkedRadioButtonId == -1) {
            showAlert("You need to mention hair growth on upper lips")
            return false
        }

        if (rdg_arms.checkedRadioButtonId == -1) {
            showAlert("You need to mention hair growth on arms")
            return false
        }

        if (rdg_inner_thighs.checkedRadioButtonId == -1) {
            showAlert("You need to mention hair growth on inner thighs")
            return false
        }

        return true
    }

    fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Alert")

        builder.setMessage(msg)


        builder.setPositiveButton("Ok", null)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)

        alertDialog.show()
    }

    fun networkcall() {
        val apiService = ApiClient.getClient()?.create(ApiInterface::class.java)

        apiService?.predict(mlinput)?.enqueue(object : Callback<PredictionResponse> {
            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                Log.i("On failure","${t.localizedMessage}")
            }

            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.code() == 200) {
                    Log.i("On success: 200","${response.body()}")

                    val builder = AlertDialog.Builder(this@MainActivity)

                    builder.setTitle("Result!")
                    if (response.body()?.output == 1) {
                        builder.setMessage("There are chances you have PCOS")
                    } else {
                        builder.setMessage("There are chances you do not have PCOS")
                    }

                    builder.setPositiveButton("Ok", null)

                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)

                    alertDialog.show()

                } else {
                    Log.i("On success: !!!!200","${response.body()}")
                }
            }
        })
    }
}
