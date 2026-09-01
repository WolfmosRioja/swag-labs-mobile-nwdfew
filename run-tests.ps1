param(
    [string]$Platform = '',
    [string]$Environment = '',
    [string]$Tags = ''
)

# Convenience wrapper to run the Swag Labs mobile automation suite sequentially
# on the connected device.
#
# Usage:
#   .\run-tests.ps1                          # Android, local (defaults) - full suite
#   .\run-tests.ps1 -Platform ios            # iOS simulator, local
#   .\run-tests.ps1 -Environment staging     # Android, staging
#   .\run-tests.ps1 -Tags @sanity            # Only @sanity scenarios (includes @smoke)
#   .\run-tests.ps1 -Tags "@smoke"           # Only @smoke scenarios
#
# The framework boots its own Appium server automatically (see AppiumServerManager),
# so no manual Appium setup is required. Node.js/Appium and the target device
# (adb device or iOS simulator) must be reachable.
#
# Note on tags: @sanity is the superset of @smoke - @smoke scenarios carry both
# tags, so a -Tags @sanity run automatically includes the smoke tier.

$ErrorActionPreference = 'Stop'

if ($Platform) { $env:PLATFORM = $Platform }
if ($Environment) { $env:ENVIRONMENT = $Environment }

$platformLabel = if ([string]::IsNullOrEmpty($env:PLATFORM)) { "android" } else { $env:PLATFORM }
$envLabel = if ([string]::IsNullOrEmpty($env:ENVIRONMENT)) { "local" } else { $env:ENVIRONMENT }

Write-Host ">>> Platform   : $platformLabel"
Write-Host ">>> Environment: $envLabel"
if ($Tags) { Write-Host ">>> Tags       : $Tags" }

if ($Tags) {
    & mvn clean test "-Dcucumber.filter.tags=$Tags"
} else {
    & mvn clean test
}
exit $LASTEXITCODE
