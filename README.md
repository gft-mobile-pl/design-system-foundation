# Design System Foundation

The Design System Foundation (DSF) library serves as a foundation for building highly stylable and extensible design systems in the Jetpack Compose environment.
It provides a structured approach to managing and organizing design tokens and component styles through:
- an extensible module container that acts as a repository for core design system elements such as colors, typography, and component styles,
- a mechanism for defining, referencing, and resolving design tokens,
- a flexible object-oriented system for creating overridable, hierarchical, declarative, and optionally dynamic styles that can reference properties from other styles,
- an integrated cache to improve performance when working with styles.

> 💡 Creating your own design system from scratch is a complex and time-consuming task.
> In most cases, a better solution is to use the [**Mobius**](https://github.com/gft-mobile-pl/mobius) library, which is based on both **Design System Foundation** and **Material Design 3**.
> 
> [**Mobius**](https://github.com/gft-mobile-pl/mobius) offers a high degree of stylability and extensibility, which in most cases allows you to achieve the desired results with significantly less effort than building a custom design system from the ground up.
> Although Mobius is based on Material Design 3, it offers far greater customization through styling mechanism, which reduces the need to override the original components.

## Documentation

The documentation is divided into two parts — the first outlines the general philosophy behind the design system, while the second is a step-by-step tutorial demonstrating how to implement a design system from scratch.

- [Anatomy of a design system](/documentation/anatomy_of_a_design_system.md)
- [How to implement a design system with Design System Foundation](/documentation/how-to-implement-design-system.md)

## Integration

Add the following line to your `build.gradle` file to include Design System Foundation in your project:
```groovy
implementation("com.gft.mobile:design-system:0.5.0")
```
To enable automatic `StyleValues` generation with `@GenerateStyleValues`, also add
```groovy
ksp("com.gft.mobile:design-system-codegen:0.5.0")
```
and configure KSP in your project.

## License
```
Copyright 2025 GFT Technologies SE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
