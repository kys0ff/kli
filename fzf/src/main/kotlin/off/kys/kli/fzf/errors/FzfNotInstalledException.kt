package off.kys.kli.fzf.errors

class FzfNotInstalledException : Exception(
    "FZF is not installed. Please install FZF first.\n" +
            "See: https://github.com/junegunn/fzf#installation"
)