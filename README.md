# DogsShowCase — Implementation Decisions, Limitations & Future Improvements

## Table of Contents

1. [Implementation Decisions](#implementation-decisions)
2. [Application Limitations](#application-limitations)
3. [Given More Time](#given-more-time)

---

## Implementation Decisions

### Architecture — Clean Architecture with Feature-Based Packaging

The project follows **clean architecture** with three clear layers:

| Layer | Package | Responsibility |
|---|---|---|
| **Data** | `features_components/data/` | API definitions (`DogBreedsApi`), response models (`DogBreedResponse`, `DogImagesResponse`), and the repository implementation (`DogBreedsRepositoryImpl`). |
| **Domain** | `features_components/domain/` | Repository interface (`DogBreedsRepository`) and use cases (`FetchDogBreedsUseCase`, `FetchDogBreedImagesUseCase`). |
| **Presentation** | `features_presentation/` | ViewModels, UI state classes, Composable screens, and navigation. |

**Why:** Separating domain from data means the UI and business logic never depend on Retrofit or any specific networking library directly. Swapping out the data source (e.g. adding a local Room database) would only require changes in the data layer and the DI module.

### Dependency Injection — Hilt

All dependencies (API, repository, use cases) are provided through a single `AppModule` annotated with `@InstallIn(SingletonComponent::class)`. ViewModels are annotated with `@HiltViewModel` and receive their use cases via constructor injection.

### Networking — Retrofit + Kotlinx Serialization

- **Retrofit** handles HTTP calls to the [Dog CEO API](https://dog.ceo/dog-api/) (`https://dog.ceo/api/`).
- **Kotlinx Serialization** is used for JSON parsing (via `jakewharton/retrofit2-kotlinx-serialization-converter`) instead of Gson/Moshi.

### State Management — `Resource<T>` Sealed Class + Flow

- A `Resource<out T>` sealed class represents three states: `Loading`, `Success<T>`, and `Error`.
- `Loading` is an `object` and `Error` returns `Resource<Nothing>`, so neither requires type arguments at the call site.
- Repository methods return `Flow<Resource<T>>`. Use cases map the raw API response into domain-friendly types (e.g. `DogBreedResponse` → `List<String>`).
- ViewModels store the flow in a mutable state object (`DogBreedState` / `DogBreedImagesState`). Composables collect it with `collectAsStateWithLifecycle`.

**Why:** Flows provide a reactive stream that integrates naturally with coroutines and Compose lifecycle. The `Resource` wrapper makes loading/error/success handling explicit and uniform across every screen.

### Navigation — Jetpack Navigation Compose

- Routes are defined in the `UiScreen` sealed class.
- `DogBreedNavigationActions` encapsulates navigation calls and uses `navigateToSingleTop` to avoid duplicate back-stack entries.
- The breed name is passed as a path argument to the images screen and retrieved via `SavedStateHandle` in the ViewModel.

**Why:** Navigation Compose is the recommended solution for Compose-based apps. Encapsulating actions in a dedicated class keeps navigation logic out of composables and enforcing single responsibility principle.

### Image Loading — Coil

Breed images are loaded asynchronously using Coil's `AsyncImage` composable.

**Why:** Coil is Kotlin-first, lightweight, and has first-class Compose support with built-in caching and cancellation.

### Testing — JUnit + MockWebServer + Google Truth + Coroutines Test

- Unit tests exist for the repository (`DogBreedsRepositoryImplTest`) and both use cases (`FetchDogBreedsUseCaseTest`, `FetchDogBreedImagesUseCaseTest`).
- `MockWebServer` provides deterministic HTTP responses from JSON fixture files stored under `src/test/resources/`.
- A custom `MainDispatchRule` replaces the Main dispatcher with `UnconfinedTestDispatcher` for deterministic coroutine execution.
- `Google Truth` is used for readable assertions.

**Why:** Testing against a fake HTTP server validates the full serialization → repository → use-case pipeline without hitting the real network. Fixture files make adding new test scenarios easy.

---

## Application Limitations

1. **No offline support / caching.**
   The app is fully network-dependent. If the device is offline the user sees only an error screen. There is no local database or disk cache.

2. **No retry mechanism.**
   When an error occurs, the user must restart the screen or the app. There is no "Retry" button on the error UI.

3. **No pagination.**
   The breeds list is loaded in a single API call. The images screen fetches a fixed count of 10 images. There is no lazy pagination for either.

4. **No search or filtering.**
   Users cannot search for a specific breed by name.

5. **No UI tests.**
While unit tests cover the data and domain layers, there are no instrumented or Compose UI tests.

6. **No view model tests.**
While unit tests cover the data and domain layers, there are no tests that verify view model updating the UI state.
---

## Given More Time

| Area | What I Would Do |
|---|---|
| **Offline support** | Add a Room database to cache breeds and images. The repository would follow an offline-first strategy: serve cached data immediately, refresh from the network in the background, and update the cache. |
| **Retry on error** | Add a "Retry" button to the error UI and expose a `retry()` action in the ViewModel that re-triggers the flow. |
| **Pagination** | Implement `Paging 3` for the images screen so users can scroll through more than 10 images. |
| **Search / filter** | Add a search bar on the breeds screen that filters the list locally as the user types. |
| **UI tests** | Write Compose UI tests (using `createComposeRule`) to verify loading spinners, error states, and successful list rendering. |
| **Pull-to-refresh** | Add swipe-to-refresh on both screens so users can manually refresh data without restarting. |
| **Sub-breeds** | The API returns sub-breeds (the value lists in the `message` map). I would display them in an expandable list or a detail section for each breed. |
| **Image detail screen** | Allow tapping an image to view it full-screen with pinch-to-zoom. |

