$baseUrl = "http://localhost:8080"
$headers = @{ "Content-Type" = "application/json" }

Write-Host "====================== TESTING CAMPAIGNX BACKEND API ======================"

# 1. Register a new user
Write-Host "`n1. Registering user 'test@campaignx.com'..."
$regBody = @{
    name = "Test User"
    email = "test@campaignx.com"
    password = "TestPassword123"
} | ConvertTo-Json
try {
    $regResponse = Invoke-WebRequest -Uri "$baseUrl/api/auth/register" -Method Post -Body $regBody -Headers $headers
    Write-Host "  Success: Registration returned Status ($($regResponse.StatusCode))"
} catch {
    if ($_.Exception.Message -like "*400*") {
        Write-Host "  Info: User already registered (400 Bad Request). Proceeding to login."
    } else {
        Write-Host "  Failed to register user: $_"
        exit 1
    }
}

# 2. Login as the new user
Write-Host "`n2. Logging in as 'test@campaignx.com'..."
$loginBody = @{
    email = "test@campaignx.com"
    password = "TestPassword123"
} | ConvertTo-Json
try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $loginBody -Headers $headers
    $userToken = $loginResponse.token
    $userRole = $loginResponse.role
    Write-Host "  Success: Received Role: $userRole"
} catch {
    Write-Host "  Failed to login: $_"
    exit 1
}

$userAuthHeader = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $userToken"
}

# 3. Get profile
Write-Host "`n3. Fetching profile for logged-in user..."
try {
    $profile = Invoke-RestMethod -Uri "$baseUrl/api/users/profile" -Method Get -Headers $userAuthHeader
    $userId = $profile.id
    Write-Host "  Success: Profile ID: $userId, Name: $($profile.name), Role: $($profile.role)"
} catch {
    Write-Host "  Failed to fetch profile: $_"
}

# 4. Update profile
Write-Host "`n4. Updating profile metadata..."
$updateBody = @{
    name = "Test User Updated"
    email = "test@campaignx.com"
} | ConvertTo-Json
try {
    $profileUpdated = Invoke-RestMethod -Uri "$baseUrl/api/users/profile" -Method Put -Body $updateBody -Headers $userAuthHeader
    Write-Host "  Success: Updated Name: $($profileUpdated.name)"
} catch {
    Write-Host "  Failed to update profile: $_"
}

# 5. Generate a campaign
Write-Host "`n5. Generating marketing campaign..."
$campaignRequest = @{
    brandName = "Aero Sneakers"
    product = "Lightweight Running Shoes"
    audience = "Marathon Runners"
    goal = "Product Launch"
    tone = "Inspiring"
} | ConvertTo-Json
try {
    $genContent = Invoke-RestMethod -Uri "$baseUrl/api/campaigns/generate" -Method Post -Body $campaignRequest -Headers $userAuthHeader
    $campaignId = $genContent.campaignId
    Write-Host "  Success: Campaign ID generated: $campaignId"
    Write-Host "  LinkedIn Post Preview: $($genContent.linkedinPost)"
    Write-Host "  SEO Keywords: $($genContent.seoKeywords -join ', ')"
} catch {
    Write-Host "  Failed to generate campaign: $_"
}

# 6. Get campaigns list
Write-Host "`n6. Querying user campaigns..."
try {
    $campaigns = Invoke-RestMethod -Uri "$baseUrl/api/campaigns" -Method Get -Headers $userAuthHeader
    Write-Host "  Success: Found $($campaigns.Count) campaigns for user. First campaign ID: $($campaigns[0].id)"
} catch {
    Write-Host "  Failed to query campaigns list: $_"
}

# 7. Get campaign detail
Write-Host "`n7. Retrieving campaign details for ID: $campaignId..."
try {
    $campaignDetail = Invoke-RestMethod -Uri "$baseUrl/api/campaigns/$campaignId" -Method Get -Headers $userAuthHeader
    Write-Host "  Success: Campaign brandName matches: $($campaignDetail.brandName)"
    Write-Host "  Instagram Caption detail: $($campaignDetail.instagramCaption)"
} catch {
    Write-Host "  Failed to query campaign detail: $_"
}

# 8. Login as Admin
Write-Host "`n8. Logging in as Admin..."
$adminLoginBody = @{
    email = "admin@campaignx.com"
    password = "AdminPassword123"
} | ConvertTo-Json
try {
    $adminLogin = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $adminLoginBody -Headers $headers
    $adminToken = $adminLogin.token
    Write-Host "  Success: Admin authenticated."
} catch {
    Write-Host "  Failed to login as admin: $_"
    exit 1
}

$adminAuthHeader = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $adminToken"
}

# 9. Get admin stats
Write-Host "`n9. Fetching admin analytics dashboard stats..."
try {
    $stats = Invoke-RestMethod -Uri "$baseUrl/api/admin/analytics" -Method Get -Headers $adminAuthHeader
    Write-Host "  Success: Total Users: $($stats.totalUsers), Total Campaigns: $($stats.totalCampaigns), Active Users: $($stats.activeUsersThisWeek)"
} catch {
    Write-Host "  Failed to fetch admin stats: $_"
}

# 10. Get admin users list
Write-Host "`n10. Fetching user list..."
try {
    $usersList = Invoke-RestMethod -Uri "$baseUrl/api/admin/users" -Method Get -Headers $adminAuthHeader
    Write-Host "  Success: Found $($usersList.Count) registered users."
} catch {
    Write-Host "  Failed to fetch users list: $_"
}

# 11. Drop campaign
Write-Host "`n11. Attempting to delete user campaign ID: $campaignId..."
try {
    $delResponse = Invoke-WebRequest -Uri "$baseUrl/api/campaigns/$campaignId" -Method Delete -Headers $userAuthHeader
    Write-Host "  Success: Deletion returned Status ($($delResponse.StatusCode))"
} catch {
    Write-Host "  Failed to delete campaign: $_"
}

Write-Host "`n====================== API TESTS COMPLETION ======================"
