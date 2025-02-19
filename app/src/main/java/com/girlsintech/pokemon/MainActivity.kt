package com.girlsintech.pokemon

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.girlsintech.pokemon.screens.ErrorMessage
import com.girlsintech.pokemon.screens.MainView
import com.girlsintech.pokemon.screens.PokemonDetailPage
import com.girlsintech.pokemon.screens.PokemonListPage
import com.girlsintech.pokemon.ui.theme.PokemonTheme
import com.girlsintech.pokemon.util.SelectedPokemon
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokemonTheme {
                val context = LocalContext.current
                val owner = this

                val viewModel: PokemonDetailViewModel =
                    viewModel(
                        factory = PokemonDetailViewModel.PokemonDetailViewModelFactory(context.applicationContext as Application)
                    )

                var refresh by rememberSaveable {
                    mutableStateOf(MyState.Load)
                }

                var message by rememberSaveable {
                    mutableStateOf("")
                }


                val navController = rememberNavController()

                NavHost(navController = navController,
                    startDestination = "pokemon_homepage"){
                    composable("pokemon_homepage"){
                        MainView { navController.navigate("pokemon_list_screen") }
                    }
                    composable("pokemon_list_screen"){
                        PokemonListPage(navController = navController)
                    }
                    composable("pokemon_detail_screen"){

                        viewModel.getData(SelectedPokemon.pokemonSelected.value!!.url) {
                            refresh = MyState.Error
                            message = it
                        }
                        viewModel.pokemonInfo.observe(owner) {
                            refresh = MyState.Success
                        }

                        when (refresh) {
                            MyState.Success -> {
                                PokemonDetailPage(
                                    dominantColor = SelectedPokemon.color.value,
                                    pokemon = SelectedPokemon.pokemonSelected.value!!,
                                    viewModel = viewModel,
                                    viewModelDb = SelectedPokemon.viewModel.value!!,
                                    navController = navController
                                )
                            }
                            MyState.Error -> {
                                ErrorMessage(message)
                            }
                            MyState.Load, MyState.Init -> {}
                        }
                    }
                }
            }
        }
    }
}