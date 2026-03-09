# MCP-git-intergration
this repo is for the mcp-git-integration

## Jira-aware PR workflow

This repository includes a GitHub Action at `.github/workflows/jira-pr-context-review.yml`.

When a pull request targets `main` or `master`, the workflow:
- extracts Jira key from PR title/body/branch (e.g. `KAN-4`)
- fetches Jira issue summary/description from Atlassian
- updates PR description with Jira context + acceptance criteria checklist
- posts/updates a Jira-context review kickoff comment
- adds a non-blocking review comment asking reviewers to review against Jira criteria

### Required GitHub repository secrets

Add these secrets in GitHub repository settings:
- `JIRA_BASE_URL` (example: `https://gourav-test-24.atlassian.net`)
- `JIRA_EMAIL` (Atlassian account email)
- `JIRA_API_TOKEN` (Atlassian API token)

If Jira key is missing from PR title/body/branch, the workflow comments and fails the check.
