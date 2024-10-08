package santos.lucas.joao.watertracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import santos.lucas.joao.watertracker.databinding.ActivityMainBinding
import santos.lucas.joao.watertracker.sqlite.WaterEntryController

class MainActivity : ComponentActivity()

    private lateinit var binding: ActivityMainBinding
    private lateinit var consumptionController: WaterEntryController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO
    }
}

