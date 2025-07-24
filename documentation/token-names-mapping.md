
## Mapping tokens names between the tools

Tokens are generally expected to be organized consistently and share the same naming conventions across all tools used throughout the project.
Unfortunately, in practice, limitations of certain tools — as well as simple convenience — often lead UX designers to create "hybrid" types of tokens
that serve the role of system tokens but are named in a way characteristic of value tokens.

### Example 1

Below is a real-world example of typography token names defined in Figma:
```
Title Larger 32
Title Large Bold 24
Title 18
Body bigger 16
Body normal 12
Body
```

Are these value tokens or system tokens?    
While their names partially resemble value tokens (e.g. Bold 24) and partially system tokens (e.g. Body Normal), 
their role in the design clearly classifies them as system tokens:
- they were used directly in layouts and components, 
- when changes were needed — such as adjusting the size of all large titles — the value of the Title Large Bold 32 token was modified, and its name was simply updated afterward.

The best solution in such a case would be to create a dedicated set of `system tokens`:
```kotlin 
// Figma
Title Large
Title Medium
Title Small
Body Large
Body Medium
Body Small

// Kotlin
internal interface ExampleTypography : Typography {
    val titleLarge: TextStyle
    val titleMedium: TextStyle
    val titleSmall: TextStyle
    val bodyLarge: TextStyle
    val bodyMedium: TextStyle
    val bodySmall: TextStyle
}
```
Then, the current tokens should be converted into `value tokens`:
```kotlin
// Figma
Pantom 32 // 💡 the name of a value token should directly describe the value
Pantom 24 Bold
Pantom 18
Arial 72
Arial 64
Arial 46
SF Pro 32
SF Pro 24
SF Pro 16
SF Pro 12
SF Pro 10

// Kotlin
internal object ExampleReferenceTypography {
    val Pantom_32: TextStyle = ... // same name as in Figma
    val Pantom_24_Bold: TextStyle = ...
    val Pantom_18: TextStyle = ...
    val Arial_72: TextStyle = ...
    val Arial_64: TextStyle = ...
    val Arial_48: TextStyle = ...
    val SF_Pro_32: TextStyle = ...
    val SF_Pro_24: TextStyle = ...
    val SF_Pro_16: TextStyle = ...
    val SF_Pro_12: TextStyle = ...
    val SF_Pro_10: TextStyle = ...
}
```
> 💡 The general naming rule for `value tokens` is that their names should describe the visual appearance of the value, not the intended purpose of the token, as `value tokens` are not intended to carry any semantic meaning.

As a final step, `value tokens` should be assigned to `system tokens` to reflect the current design system decisions. 
Note that the bindings may vary depending on the context (e.g., in accessibility mode different fonts may be used):
```kotlin
internal open class DefaultExampleTypography : ExampleTypography {
    override val titleLarge: TextStyle = ExampleReferenceTypography.Pantom_32
    override val titleMedium: TextStyle = ExampleReferenceTypography.Pantom_24_Bold
    override val titleSmall: TextStyle = ExampleReferenceTypography.Pantom_18
    override val bodyLarge: TextStyle = ExampleReferenceTypography.SF_Pro_16
    override val bodyMedium: TextStyle = ExampleReferenceTypography.SF_Pro_12
    override val bodySmall: TextStyle = ExampleReferenceTypography.SF_Pro_10
}

internal open class ExampleAccessibilityTypography : ExampleTypography {
    override val titleLarge: TextStyle = ExampleReferenceTypography.Pantom_32
    override val titleMedium: TextStyle = ExampleReferenceTypography.Pantom_24_Bold
    override val titleSmall: TextStyle = ExampleReferenceTypography.Pantom_18
    override val bodyLarge: TextStyle = ExampleReferenceTypography.Arial_72
    override val bodyMedium: TextStyle = ExampleReferenceTypography.Arial_64
    override val bodySmall: TextStyle = ExampleReferenceTypography.Arial_48
}
```

> 💡 In Figma, token assignments are managed through variables.    
> Refer to the Figma documentation for more details: [Update 1: Tokens, variables, and styles](https://help.figma.com/hc/en-us/articles/18490793776023-Update-1-Tokens-variables-and-styles#token-organization).

#### Alternative approach

If the Figma design has already been finalized or there are no resources available for further edits, it is possible to adapt to it only on the Compose side.

First define the `value tokens`: 
> ⚠ Note that token names in Kotlin match original Figma names, even though they break naming rules for typography value tokens.
```kotlin
// Figma - value tokens (they are defined already)
Title Larger 32
Title Large Bold 24
Title 18
Body bigger 16
Body normal 12
Body

// Kotlin - value tokens
internal object ExampleReferenceTypography {
    val Title_Larger_32: TextStyle = ... // 
    val Title_Large_Bold_24: TextStyle = ...
    val Title_18: TextStyle = ...
    val Body_bigger_16: TextStyle = ...
    val Body_normal_12: TextStyle = ...
    val Body: TextStyle = ...            
}
```
The, define `system tokens` only on Kotlin side:
```kotlin
// Kotlin - system tokens
internal interface ExampleTypography : Typography {
    val titleLarge: TextStyle
    val titleMedium: TextStyle
    val titleSmall: TextStyle
    val bodyLarge: TextStyle
    val bodyMedium: TextStyle
    val bodySmall: TextStyle
}
```
Next, bind the appropriate `value tokens` to the `system tokens`. 
```kotlin
open class DefaultTypography : ExampleTypography {
    override val titleLarge: TextStyle = ExampleReferenceTypography.Title_Larger_32
    override val titleMedium: TextStyle = ExampleReferenceTypography.Title_Large_Bold_24
    override val titleSmall: TextStyle = ExampleReferenceTypography.Title_18
    override val bodyLarge: TextStyle = ExampleReferenceTypography.Body_bigger_16
    override val bodyMedium: TextStyle = ExampleReferenceTypography.Body_normal_12
    override val bodySmall: TextStyle = ExampleReferenceTypography.Body
}
```
This approach ensures proper token categorization on the Compose side while still making it easy to trace which token in the code corresponds to which token in Figma.

### Example 2

Another common issue involves the naming of `value tokens` representing colors. 
More than ofter the semantic meaning is included in the token name, while `value token` names should describe the actual value, not its intended usage.

The following example highlights a frequent issue in projects using Material Design 3 regarding how `value tokens` are defined:
```kotlin
// Figma - value tokens
Primary 0
Primary 10
Primary 20
...
Primary 100
...
Error 0
Error 10
Error 20
...
Error 100
...
Yellow 0
Yellow 10
Yellow 20
...
Yellow 100

// Kotlin - value tokens
internal object ExampleReferenceColors {
    val Primary0 = Color(0xff000000)
    val Primary10 = Color(0xff000E33)
    val Primary20 = Color(0xff001849)
    ...
    val Primary100 = Color(0xffFFFFFF)
    ...
    val Error0 = Color(0xffff0000)
    val Error10 = Color(0xffff3030)
    val Error20 = Color(0xffff6060)
    ...
    val Error100 = Color(0xffffffff)
    ...
    val Yellow0 = Color(0xffdddd00)
    val Yellow10 = Color(0xffdddd30)
    val Yellow20 = Color(0xffdddd60)
    ...
    val Yellow100 = Color(0xffffffff)
}
```

```kotlin
// System/semantic token declarations
@Immutable
interface ExampleColorScheme : Colors {
    val primary: Color
    val secondary: Color
    val highlight: Color
    val error: Color
    val errorContainer: Color
    val dangerousOperation: Color
}

// System/semantic token definitions
open class ExampleLightScheme : ExampleColorScheme {
    override val primary: Color = ExampleReferenceColors.Primary20
    override val secondary: Color = ExampleReferenceColors.Secondary20
    override val highlight: Color = ExampleReferenceColors.Primary80
    override val error: Color = ExampleReferenceColors.Error20
    override val errorContainer: Color = ExampleReferenceColors.Error90
    override val dangerousOperation: Color = ExampleReferenceColors.Error40
}

open class ExampleAccessibilityScheme : ExampleColorScheme {
    override val primary: Color = ExampleReferenceColors.Yellow40
    override val secondary: Color = ExampleReferenceColors.Secondary20
    override val highlight: Color = ExampleReferenceColors.White
    override val error: Color = ExampleReferenceColors.Error20
    override val errorContainer: Color = ExampleReferenceColors.Error90
    override val dangerousOperation: Color = ExampleReferenceColors.Error40
}
```
In the example above, several issues can be observed:
- The `value tokens` in the `Primary0`–`Primary100` gradient have a semantic prefix `Primary...`, suggesting that they represent the primary color.
In reality, this only holds true for the `ExampleLightScheme`. In the case of the `ExampleAccessibilityScheme`, the primary token uses a color from an entirely different color family.
- The `Error0`–`Error100` family consists of value tokens that define a red gradient. Despite the `Error...` prefix, this gradient is used not only for tokens semantically related to errors, but also for marking dangerous operations (like deleting the account).
- The naming of value tokens is inconsistent — some names carry semantic meaning, while others do not (e.g., the `Yellow...` family).

Again, the general naming rule for value tokens is that their names should describe the visual appearance of the value, not the intended purpose of the token, as value tokens are not intended to carry any semantic meaning.

The best solution would be renaming values tokens both in Figma and Kotlin:
```kotlin
// Figma - value tokens
Royal Blue 0 // instead of Primary 0
Royal Blue 10
Royal Blue 20
...
Royal Blue 100
...
Red 0 // instead of Error 0
Red 10
Red 20
...
Red100
...
Yellow 0
Yellow 10
Yellow 20
...
Yellow 100

// Kotlin - value tokens
internal object ExampleReferenceColors {
    val RoyalBlue0 = Color(0xff000000) // instead of Primary0 
    val RoyalBlue10 = Color(0xff000E33)
    val RoyalBlue20 = Color(0xff001849)
    ...
    val RoyalBlue100 = Color(0xffFFFFFF)
    ...
    val Red0 = Color(0xffff0000) // instead of Error0
    val Red10 = Color(0xffff3030)
    val Red20 = Color(0xffff6060)
    ...
    val Red100 = Color(0xffffffff)
    ...
    val Yellow0 = Color(0xffdddd00)
    val Yellow10 = Color(0xffdddd30)
    val Yellow20 = Color(0xffdddd60)
    ...
    val Yellow100 = Color(0xffffffff)
}
```

```kotlin
// System/semantic token definitions
open class ExampleLightScheme : ExampleColorScheme {
    override val primary: Color = ExampleReferenceColors.RoyalBlue20
    override val secondary: Color = ExampleReferenceColors.MidnightBlue20
    override val highlight: Color = ExampleReferenceColors.RoyalBlue80
    override val error: Color = ExampleReferenceColors.Red20
    override val errorContainer: Color = ExampleReferenceColors.Red90
    override val dangerousOperation: Color = ExampleReferenceColors.Red40
}

open class ExampleAccessibilityScheme : ExampleColorScheme {
    override val primary: Color = ExampleReferenceColors.Yellow40
    override val secondary: Color = ExampleReferenceColors.MidnightBlue20
    override val highlight: Color = ExampleReferenceColors.White
    override val error: Color = ExampleReferenceColors.Red20
    override val errorContainer: Color = ExampleReferenceColors.Red90
    override val dangerousOperation: Color = ExampleReferenceColors.Red40
}
```
If renaming tokens in Figma is no longer feasible (e.g., due to limited resources or time), the original names should be preserved, 
as maintaining consistency between Figma and Kotlin in the naming of value/reference tokens is the preferred approach.
