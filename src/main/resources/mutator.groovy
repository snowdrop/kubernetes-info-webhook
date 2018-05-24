import me.snowdrop.kubernetes.info.webhook.script.ScriptRunner

final ScriptRunner.Input input = binding.getVariable("input")
final Map<String, Object> originalObjectCopy = input.originalObjectCopy

//don't perform any changes

return originalObjectCopy