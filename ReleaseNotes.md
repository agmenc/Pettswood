Bureaucrats only. All others please ignore

0.0.19
Specify conceptorisers as call-by-name, to remove uglier () => syntax
Make Results a trait

0.0.10
Users can specify mixin packages, by overriding or adding to PettswoodConfig.mixinPackages
Underlying exceptions that cause mixin invocation failures are displayed, rather than the (unhelpful) InvocationTargetException

0.0.9
Allow users to specify the location of their test files, by overriding PettswoodConfig settings in their integration hook
Generate output to the correct place on Windows
Invoked probes are now always removed from the probe stack, even when they fail
The title hyperlink now works one-way, from source to target.