<template>
  <div class="m-auto" style="max-width: 400px">
    <div class="text-start p-2">
      <h2 class="m-2">
        Edit <span style="font-family: 'Arial Black', serif">Member</span>
      </h2>
      <div class="row p-2">
        <div class="col">
          <form @submit.prevent="updateUser">
            <div class="mb-2">
              <label for="status" class="form-label"> Member Authority </label>
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="authority-radio"
                  id="unverified-radio"
                  value="USER"
                  v-model="userForm.authority"
                />
                <label class="form-check-label" for="unverified-radio">
                  Unverified
                </label>
              </div>
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="authority-radio"
                  id="verified-radio"
                  value="USER_VERIFIED"
                  v-model="userForm.authority"
                />
                <label class="form-check-label" for="verified-radio">
                  Verified
                </label>
              </div>
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="authority-radio"
                  id="suspended-radio"
                  value="USER_SUSPENDED"
                  v-model="userForm.authority"
                />
                <label class="form-check-label" for="suspended-radio">
                  Suspended
                </label>
              </div>
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="authority-radio"
                  id="admin-radio"
                  value="ADMIN"
                  v-model="userForm.authority"
                />
                <label class="form-check-label" for="admin-radio">
                  Administrator (grant ALL PRIVILEGES)
                </label>
              </div>
            </div>
            <button
              type="submit"
              class="btn btn-primary mt-2"
              @click="userForm.error = null"
            >
              Update Member
            </button>
            <button
              class="btn btn-secondary mt-2 ms-2"
              @click.prevent="$router.go(-1)"
            >
              Cancel
            </button>
            <div
              class="alert alert-danger alert-dismissible fade show mt-3"
              role="alert"
              v-if="userForm.error"
            >
              <strong>Error:</strong> {{ userForm.error }}
              <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert"
                aria-label="Close"
                @click="userForm.error = null"
              ></button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserRepository from "@/repositories/UserRepository";

export default {
  name: "UserEdit",
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      userForm: {
        id: "",
        authority: "",
        error: null,
      },
    };
  },
  methods: {
    async updateUser() {
      try {
        await UserRepository.saveAuthority(this.userForm);
        this.$router.go(-1);
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.userForm.error = response.message;
        console.error(err);
      }
    },
  },
  mounted() {
    this.userForm.id = this.user.id;
    this.userForm.authority = this.user.authority;
  },
};
</script>

<style scoped></style>
