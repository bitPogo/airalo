## 🚀 Features

- Modern UI using Jetpack Compose
- TDD by core and all what comes with it
- Dependency Injection with Koin
- Command Pattern in action
- OpenAPI specification for API documentation
- Convention Plugins
- Accessibility friendly (try TalkBack with English!)

## 🏗 Project Structure

The project follows a modular architecture with the following:

- `:app` -Umbrella for the application
- `:offer` - Feature module for offer-related functionality (aka the requirements for this sample)
- `:command` - Command pattern definition
- `:test` - Testing helpers
  - `:test:mockclientfactory` - Mock/Fake client factory of Ktor
  - `:test:roborazzi` - Screenshot test utilities

## 🛠 Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Declarative UI/Atomic Design, Command Pattern and what is commonly know as Clean Architecture
- **Dependency Injection**: Koin
- **Navigation**: Jetpack Navigation Compose
- **Build System**: Gradle with Kotlin DSL
- **Api Integration**: OpenAPI specification

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Android SDK 34 or later
- Android SDK commandline tools (optional/for Acceptance tests)
- [Maestro](https://docs.maestro.dev/) (optional/for Acceptance tests)
- A pre setup Android emulator which is call `MaestroTestRunner`

## 📦 Dependencies

The project uses version catalogs for dependency management:

- `runtime.versions.toml` - Runtime dependencies
- `build.versions.toml` - Build dependencies
- `test.versions.toml` - Testing dependencies

## 🏗 Build Configuration

The project uses Gradle with Kotlin DSL and includes:

- Static analysis with Spotless
- Code coverage with Kover
- Quality metrics
- Convention plugins

## 🚀 Executable Commands

### Build Commands
```bash
# Build the entire project
./gradlew build

# Build specific module
./gradlew :app:build
./gradlew :offer:build
./gradlew :command:build

# Clean build
./gradlew clean build
```

### Test Commands
```bash
# Run all tests
./gradlew test

# Run specific module tests
./gradlew :app:test
./gradlew :offer:test

# Run UI tests
./gradlew :app:connectedAndroidTest

# Run screenshot tests
./gradlew :test:roborazzi:test

# Run acceptance tests
./acceptanceTest/test.sh
```

### Code Quality Commands
```bash
# Run static analysis
./gradlew spotlessCheck

# Format code
./gradlew spotlessApply

# Generate code coverage report
./gradlew koverHtmlReport
```

### Development Commands
```bash
# List all available tasks
./gradlew tasks
```

### OpenAPI Commands
```bash
# Generate API client code from OpenAPI specification
./gradlew openApiGenerate

# Generate infrastructure code
./gradlew openApiInfrastructureGenerate
```

### Security Commands
```bash
# Run OWASP dependency check
./gradlew :app:owaspDependencyCheck

# Generate OWASP report
./gradlew :app:owaspDependencyCheckReport

# Run license check
./gradlew :app:licenseCheck

# Generate license report
./gradlew :app:licenseReport
```

## ⚠️ Known Issues
### Build and Development
- You might see a couple of warning - the project uses a outdated Spotless configuration
- The convention Plugins causing some warning due to the dependency resolution
- You might encounter an issue that the OpenApi. In a few instances it happened that the infrastructure was deleted by the client generation.
 if this is the case please run `./gradlew openApiInfrastructureGenerate` which should resolve the problem.
- Maestro cannot deal with multiple emulators

### Visual Regression Tests
The following screenshot tests are known to fail due to platform-specific rendering differences:
- `CountryListSpec`
- `OfferListSpec`
- `CountryOfferOverviewSpec`
- `OfferPackageScreenSpec`

These tests might fail due to differences in how different platforms render the UI components. This is a known limitation of visual regression testing across different environments.
For now the Visual Regression Tests are disabled.
To reasonable them change the following properties to true:
- `roborazzi.test.compare`
- `roborazzi.test.verify`
