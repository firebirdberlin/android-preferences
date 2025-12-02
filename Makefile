# Makefile for publishing the library

# Get the version from build.gradle, assuming a line like: version = "x.y.z"
VERSION := $(shell grep -m 1 'version = "' preferences/build.gradle | sed -e 's/.*version = "\(.*\)"/\1/')
TAG := $(VERSION)
BRANCH := $(shell git rev-parse --abbrev-ref HEAD)

.PHONY: publish

publish:
	@if [ "$(BRANCH)" != "master" ]; then \
		echo "Error: Publishing is only allowed from the master branch."; \
		exit 1; \
	fi
	@echo "Version from build.gradle: $(VERSION)"
	@echo "Git tag to be created: $(TAG)"
	@echo "Current branch: $(BRANCH)"
	@# Check if the tag already exists locally
	@if git rev-parse $(TAG) >/dev/null 2>&1; then \
		echo "Tag $(TAG) already exists. Nothing to do."; \
	else \
		echo "Pushing current branch to origin..."; \
		git push origin $(BRANCH); \
		echo "Tag $(TAG) does not exist. Creating and pushing tag."; \
		git tag -a $(TAG) -m "Release $(VERSION)"; \
		git push origin $(TAG); \
		echo "Tag $(TAG) created and pushed to origin."; \
	fi
