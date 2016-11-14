class Bicycle < ApplicationRecord
	has_many :rentals, dependent: :destroy
	validates :serial, presence: true
end
