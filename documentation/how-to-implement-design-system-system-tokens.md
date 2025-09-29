# Defining _system tokens_

Once the _value tokens_ are defined (at least roughly), the next step is to create _system tokens_, which assign semantic roles to _value tokens_ within the design system.

While defining _system tokens_ keep in mind:

1. _System tokens_ should reference _value tokens_. In general, they should not have hardcoded values assigned.

2. The same _System token_ may reference different _value tokens_ depending on the context (e.g. light / dark theme).

3. _System token_ names should reflect their meaning and purpose, as well as how and where they should be used. The name should not describe the token’s value, since that may change depending on the context.

   > 💡 Material Design 3 and Mobius (based on M3) define _system tokens_ such as `titleLarge`, `titleMedium`, and `titleSmall`, which at first glance seem to contradict this rule.
   >  The naming is unfortunate, but the authors’ intention was that the `Large`, `Medium`, and `Small` suffixes indicate relative importance of titles rather than their appearance.

4. Keep the set of _system tokens_ minimal:
   1. Define a _system token_ only if it is reused in multiple places.
   2. If some _system token_ is unused, remove it.
   3. Avoid creating a _system token_ just because a single `Style` or layout references it. In some cases, it is acceptable to reference a _value token_ directly.

5. _System tokens_ should remain consistent across all tools used in the design process.

   The first iterations of a design system are usually created using tools like Figma or Zeplin.
   Although such tools typically support defining both _value tokens_ and _system tokens_,
   UX designers often omit one of these categories or merge their responsibilities in practice.   
   A short guide on mapping token names between tools like Figma and the Compose implementation can be found [here](/documentation/token-names-mapping.md).

6. _System tokens_ are usually grouped using `Modules` or `Styles`.
















