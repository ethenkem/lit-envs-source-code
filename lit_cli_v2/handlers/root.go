package handlers

import (
	"fmt"
	"os"

	"github.com/spf13/cobra"
)

var rootCmd = &cobra.Command{
	Use:   "lit",
	Short: "Lit Envs CLI",
	Long:  `Lit Envs is a tool to sync and manage your .env files.`,
	// Run: func(cmd *cobra.Command, args []string) {
		// Do Stuff Here
	// },
}

func Execute() {
	if err := rootCmd.Execute(); err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
}
